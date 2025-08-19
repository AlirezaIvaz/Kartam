package ir.alirezaivaz.kartam.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

object CryptoHelper {
    fun encrypt(value: String): String {
        val key = KeyStoreHelper.getOrCreateSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(value.toByteArray())
        // Store IV + ciphertext together
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(value: String): String {
        val data = Base64.decode(value, Base64.DEFAULT)
        val iv = data.copyOfRange(0, 12) // GCM IV is 12 bytes
        val encrypted = data.copyOfRange(12, data.size)

        val key = KeyStoreHelper.getOrCreateSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return String(cipher.doFinal(encrypted))
    }
}
