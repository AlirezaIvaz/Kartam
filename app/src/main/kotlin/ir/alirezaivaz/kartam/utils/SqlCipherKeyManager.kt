package ir.alirezaivaz.kartam.utils

import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SqlCipherKeyManager constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    init {
        initialize()
    }

    private fun initialize() {
        generateKeystoreKeyIfNeeded()
        if (!sharedPreferences.contains("encrypted_key")) {
            generateAndEncryptSqlCipherKey()
        }
    }

    private fun generateKeystoreKeyIfNeeded() {
        if (!keyStore.containsAlias("sqlcipher_keystore_key")) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenSpec = KeyGenParameterSpec.Builder(
                "sqlcipher_keystore_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenSpec)
            keyGenerator.generateKey()
        }
    }

    private fun generateAndEncryptSqlCipherKey() {
        val secretKey = getSecretKey("sqlcipher_keystore_key")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val sqlCipherKey = ByteArray(32)
        SecureRandom().nextBytes(sqlCipherKey)

        val encryptedKey = cipher.doFinal(sqlCipherKey)
        val iv = cipher.iv

        sharedPreferences.edit {
            putString("encrypted_key", Base64.encodeToString(encryptedKey, Base64.NO_WRAP))
            putString("encryption_iv", Base64.encodeToString(iv, Base64.NO_WRAP))
        }

        // Zero out the key in memory
        sqlCipherKey.fill(0)
    }

    private fun getDecryptedSqlCipherKey(keyAlias: String, key: String, iv: String): ByteArray {
        val encryptedKey = Base64.decode(key, Base64.NO_WRAP)
        val ivBytes = Base64.decode(iv, Base64.NO_WRAP)

        val secretKey = getSecretKey(keyAlias)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, ivBytes))

        return cipher.doFinal(encryptedKey)
    }

    private fun getSecretKey(keyAlias: String): SecretKey =
        (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

    fun getSupportFactory(): SupportOpenHelperFactory {
        val encryptedKey = sharedPreferences.getString("encrypted_key", null).orEmpty()
        val iv = sharedPreferences.getString("encryption_iv", null).orEmpty()
        val decryptedKey = getDecryptedSqlCipherKey("sqlcipher_keystore_key", encryptedKey, iv)
        return DisposableKeySupportFactory(decryptedKey)
    }
}
