package ir.alirezaivaz.kartam.utils

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.ShareUiState
import ir.alirezaivaz.kartam.extensions.toPersianDigits
import java.util.Locale

object Utils {

    fun getCardNumber(number: String?): String {
        val sanitized = number.orEmpty().take(16)
        val padded = sanitized.padEnd(16, '-')
        return padded.chunked(4).joinToString(" ")
    }

    fun getCvv2(cvv2: String?, isVisible: Boolean): String {
        if (cvv2 != null) {
            if (isVisible) {
                return cvv2.takeIf { it.length in 3..4 } ?: cvv2.padStart(3, '0')
            }
            val length = cvv2.length.takeIf { it in 3..4 } ?: 3
            return getAsteriskString(length)
        }
        return "".padStart(3, '0')
    }

    fun getAsteriskString(length: Int): String {
        return "*".repeat(length)
    }

    fun getDeviceLanguage(context: Context): String {
        val locale = getSystemLocale(context)
        return normalizeLanguageTag(locale)
    }

    private fun getSystemLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }

    private fun normalizeLanguageTag(locale: Locale): String {
        val tag = locale.toLanguageTag()
        if (tag.split("-").size > 2) {
            return Locale.Builder()
                .setLanguage(locale.language)
                .setRegion(locale.country)
                .build()
                .toLanguageTag()
        }
        return tag
    }

    fun isStoragePermissionGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isStoragePermissionRequestRationale(activity: Activity?): Boolean {
        if (activity == null) return false
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun buildShareText(
        card: CardInfo,
        state: ShareUiState,
        isForeign: Boolean,
        resources: Resources
    ): String {
        val ownerName = card.englishName?.takeIf { isForeign && it.isNotBlank() } ?: card.name
        val bankName = resources.getString(card.bank.title)
        val formattedCardNumber = if (state.useCardSeparator) {
            card.number.chunked(4).joinToString("-")
        } else {
            card.number
        }

        if (state.advancedFormat) {
            return buildAdvancedShareText(
                card = card,
                ownerName = ownerName,
                bankName = bankName,
                cardNumber = formattedCardNumber,
                state = state,
                resources = resources
            )
        } else {
            return buildSimpleShareText(
                card = card,
                ownerName = ownerName,
                bankName = bankName,
                cardNumber = formattedCardNumber,
                state = state,
                resources = resources
            )
        }
    }

    private fun buildSimpleShareText(
        card: CardInfo,
        ownerName: String,
        bankName: String,
        cardNumber: String,
        state: ShareUiState,
        resources: Resources
    ): String {
        return buildString {
            if (state.includeCardNumber) {
                appendLine(cardNumber.toPersianDigits(state.usePersianDigits))
            }
            if (state.includeShabaNumber && card.shabaNumber != null) {
                appendLine(resources.getString(
                    R.string.formatter_shaba_number,
                    card.shabaNumber.toPersianDigits(state.usePersianDigits)
                ))
            }
            if (state.includeAccountNumber && card.accountNumber != null) {
                appendLine(resources.getString(
                    R.string.share_simple_account_number,
                    card.accountNumber.toPersianDigits(state.usePersianDigits)
                ))
            }
            if (state.includeOwnerName) {
                appendLine(resources.getString(
                    R.string.share_simple_owner_name,
                    ownerName
                ))
            }
            if (state.includeBankName) {
                appendLine(resources.getString(
                    R.string.share_simple_bank_name,
                    bankName
                ))
            }
        }.trim()
    }

    private fun buildAdvancedShareText(
        card: CardInfo,
        ownerName: String,
        bankName: String,
        cardNumber: String,
        state: ShareUiState,
        resources: Resources
    ): String {
        return buildString {
            appendLine(resources.getString(R.string.share_advanced_title))
            appendLine()
            if (state.includeOwnerName) {
                appendLine(resources.getString(
                    R.string.share_advanced_owner_name,
                    ownerName
                ))
            }
            if (state.includeBankName) {
                appendLine(resources.getString(
                    R.string.share_advanced_bank_name,
                    bankName
                ))
            }
            if (state.includeCardNumber) {
                appendLine(resources.getString(
                    R.string.share_advanced_card_number,
                    cardNumber.toPersianDigits(state.usePersianDigits)
                ))
            }
            if (state.includeAccountNumber && card.accountNumber != null) {
                appendLine(resources.getString(
                    R.string.share_advanced_account_number,
                    card.accountNumber.toPersianDigits(state.usePersianDigits)
                ))
            }
            if (state.includeShabaNumber && card.shabaNumber != null) {
                appendLine(resources.getString(
                    R.string.share_advanced_shaba_number,
                    card.shabaNumber.toPersianDigits(state.usePersianDigits)
                ))
            }
        }.trim()
    }

    suspend fun copyTextToClipboard(
        clipboard: Clipboard,
        label: String,
        text: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        try {
            val clipData = ClipData.newPlainText(label, text)
            val clipEntry = ClipEntry(clipData)
            clipboard.setClipEntry(clipEntry)
            onSuccess()
        } catch (_: Exception) {
            onFailure()
        }
    }

    fun shareText(
        text: String,
        context: Context,
        resources: Resources,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        try {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.app_name))
                putExtra(Intent.EXTRA_TEXT, text)
            }
            val shareIntent = Intent.createChooser(
                sendIntent,
                resources.getString(R.string.action_share_via)
            )
            context.startActivity(shareIntent)
            onSuccess()
        } catch (_: Exception) {
            onFailure()
        }
    }
}
