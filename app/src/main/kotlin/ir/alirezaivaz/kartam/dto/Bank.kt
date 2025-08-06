package ir.alirezaivaz.kartam.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ir.alirezaivaz.kartam.R

enum class Bank(
    @param:DrawableRes
    val icon: Int,
    @param:DrawableRes
    val logo: Int,
    @param:StringRes
    val title: Int,
    val isNeo: Boolean = false,
    val prefixes: List<String> = emptyList(),
    val nonPrefixes: List<String> = emptyList(),
) {
    Bankino(
        logo = R.drawable.full_logo_bankino,
        icon = R.drawable.ic_logo_bankino,
        title = R.string.bank_bankino,
        isNeo = true,
        prefixes = listOf("58594710"),
    ),
    BluBank(
        logo = R.drawable.full_logo_blu,
        icon = R.drawable.ic_logo_blu,
        title = R.string.bank_blu,
        isNeo = true,
        prefixes = listOf("62198618", "62198619")
    ),
    HiBank(
        logo = R.drawable.full_logo_hibank,
        icon = R.drawable.ic_logo_hibank,
        title = R.string.bank_hibank,
        isNeo = true,
        prefixes = listOf(),
    ),
    MetaBank(
        logo = R.drawable.full_logo_metabank,
        icon = R.drawable.ic_logo_metabank,
        title = R.string.bank_metabank,
        isNeo = true,
        prefixes = listOf(),
    ),
    Wepod(
        logo = R.drawable.full_logo_wepod,
        icon = R.drawable.ic_logo_wepod,
        title = R.string.bank_wepod,
        isNeo = true,
        prefixes = listOf("50222915"),
    ),
    Ansar(
        logo = R.drawable.ic_logo_ansar,
        icon = R.drawable.ic_logo_ansar,
        title = R.string.bank_ansar,
        prefixes = listOf("627381")
    ),
    Ayandeh(
        logo = R.drawable.full_logo_ayandeh,
        icon = R.drawable.ic_logo_ayandeh,
        title = R.string.bank_ayandeh,
        prefixes = listOf("636214")
    ),
    Central(
        logo = R.drawable.full_logo_central,
        icon = R.drawable.ic_logo_central,
        title = R.string.bank_central,
        prefixes = listOf("636795")
    ),
    Day(
        logo = R.drawable.full_logo_day,
        icon = R.drawable.ic_logo_day,
        title = R.string.bank_day,
        prefixes = listOf("502938")
    ),
    EghtesadNovin(
        logo = R.drawable.full_logo_eghtesad_novin,
        icon = R.drawable.ic_logo_eghtesad_novin,
        title = R.string.bank_eghtesad_novin,
        prefixes = listOf("627412")
    ),
    Gardeshgari(
        logo = R.drawable.full_logo_gardeshgari,
        icon = R.drawable.ic_logo_gardeshgari,
        title = R.string.bank_gardeshgari,
        prefixes = listOf("505416")
    ),
    IranVenezuelaBiNationalBank(
        logo = R.drawable.full_logo_iran_venezuela,
        icon = R.drawable.ic_logo_iran_venezuela,
        title = R.string.bank_iran_venezuela,
        prefixes = listOf("581874")
    ),
    IranZamin(
        logo = R.drawable.full_logo_iranzamin,
        icon = R.drawable.ic_logo_iranzamin,
        title = R.string.bank_iran_venezuela,
        prefixes = listOf("505785")
    ),
    Karafarin(
        logo = R.drawable.full_logo_karafarin,
        icon = R.drawable.ic_logo_karafarin,
        title = R.string.bank_karafarin,
        prefixes = listOf("502910", "627488")
    ),
    Keshavarzi(
        logo = R.drawable.full_logo_keshavarzi,
        icon = R.drawable.ic_logo_keshavarzi,
        title = R.string.bank_keshavarzi,
        prefixes = listOf("603770", "639217")
    ),
    Khavarmianeh(
        logo = R.drawable.full_logo_khavarmianeh,
        icon = R.drawable.ic_logo_khavarmianeh,
        title = R.string.bank_khavarmianeh,
        prefixes = listOf("585947"),
        nonPrefixes = Bankino.prefixes
    ),
    Maskan(
        logo = R.drawable.full_logo_maskan,
        icon = R.drawable.ic_logo_maskan,
        title = R.string.bank_maskan,
        prefixes = listOf("628023")
    ),
    Mehr(
        logo = R.drawable.full_logo_mehr,
        icon = R.drawable.ic_logo_mehr,
        title = R.string.bank_mehr,
        prefixes = listOf("606373")
    ),
    Melal(
        logo = R.drawable.full_logo_melal,
        icon = R.drawable.ic_logo_melal,
        title = R.string.bank_melal,
        prefixes = listOf("606256")
    ),
    Mellat(
        logo = R.drawable.full_logo_mellat,
        icon = R.drawable.ic_logo_mellat,
        title = R.string.bank_mellat,
        prefixes = listOf("610433", "991975")
    ),
    Melli(
        logo = R.drawable.full_logo_melli,
        icon = R.drawable.ic_logo_melli,
        title = R.string.bank_melli,
        prefixes = listOf("603799")
    ),
    Noor(
        logo = R.drawable.full_logo_noor,
        icon = R.drawable.ic_logo_noor,
        title = R.string.bank_noor,
        prefixes = listOf("507677")
    ),
    Parsian(
        logo = R.drawable.full_logo_parsian,
        icon = R.drawable.ic_logo_parsian,
        title = R.string.bank_parsian,
        prefixes = listOf("622106", "627884", "639194")
    ),
    Pasargad(
        logo = R.drawable.full_logo_pasargad,
        icon = R.drawable.ic_logo_pasargad,
        title = R.string.bank_pasargad,
        prefixes = listOf("502229", "639347"),
        nonPrefixes = Wepod.prefixes
    ),
    PostBank(
        logo = R.drawable.full_logo_postbank,
        icon = R.drawable.ic_logo_postbank,
        title = R.string.bank_postbank,
        prefixes = listOf("627760")
    ),
    Refah(
        logo = R.drawable.full_logo_refah,
        icon = R.drawable.ic_logo_refah,
        title = R.string.bank_refah,
        prefixes = listOf("589463")
    ),
    Resalat(
        logo = R.drawable.full_logo_resalat,
        icon = R.drawable.ic_logo_resalat_icon,
        title = R.string.bank_resalat,
        prefixes = listOf("504172")
    ),
    Saderat(
        logo = R.drawable.full_logo_saderat,
        icon = R.drawable.ic_logo_saderat,
        title = R.string.bank_saderat,
        prefixes = listOf("603769"),
    ),
    Saman(
        logo = R.drawable.full_logo_saman,
        icon = R.drawable.ic_logo_saman,
        title = R.string.bank_saman,
        prefixes = listOf("621986"),
        nonPrefixes = BluBank.prefixes
    ),
    SanatVaMaadan( // Rename to Sanat va Madan
        logo = R.drawable.full_logo_sanat_maadan,
        icon = R.drawable.ic_logo_sanat_maadan,
        title = R.string.bank_sanat_maadan,
        prefixes = listOf("627961")
    ),
    Sarmayeh(
        logo = R.drawable.full_logo_sarmayeh,
        icon = R.drawable.ic_logo_sarmayeh,
        title = R.string.bank_sarmayeh,
        prefixes = listOf("639607")
    ),
    Sepah(
        logo = R.drawable.full_logo_sepah,
        icon = R.drawable.ic_logo_sepah,
        title = R.string.bank_sepah,
        prefixes = listOf("589210")
    ),
    Shahr(
        logo = R.drawable.full_logo_shahr,
        icon = R.drawable.ic_logo_shahr,
        title = R.string.bank_shahr,
        prefixes = listOf("502806", "504706")
    ),
    Sina(
        logo = R.drawable.full_logo_sina,
        icon = R.drawable.ic_logo_sina,
        title = R.string.bank_sina,
        prefixes = listOf("639346")
    ),
    Tejarat(
        logo = R.drawable.full_logo_tejarat,
        icon = R.drawable.ic_logo_tejarat,
        title = R.string.bank_tejarat,
        prefixes = listOf("585983", "627353")
    ),
    ToseSaderat(
        logo = R.drawable.full_logo_tose_saderat,
        icon = R.drawable.ic_logo_tose_saderat,
        title = R.string.bank_tose_saderat,
        prefixes = listOf("627648")
    ),
    ToseTaavon(
        logo = R.drawable.full_logo_tose_taavon,
        icon = R.drawable.ic_logo_tose_taavon,
        title = R.string.bank_tose_taavon,
        prefixes = listOf("502908")
    ),
    Unknown(
        logo = R.drawable.logo_unknown,
        icon = R.drawable.logo_unknown,
        title = R.string.bank_unknown,
    );

    companion object {
        private val allBanks = entries.filter { it != Unknown }

        fun fromCardNumber(cardNumber: String): Bank {
            val cardPrefix = cardNumber.take(8)

            for (bank in allBanks) {
                if (bank.prefixes.any { cardPrefix.startsWith(it) }) {
                    return bank
                }
            }

            val fallbackPrefix = cardNumber.take(6)
            for (bank in allBanks) {
                if (
                    bank.prefixes.any { fallbackPrefix.startsWith(it) } &&
                    bank.nonPrefixes.none { cardPrefix.startsWith(it) }
                ) {
                    return bank
                }
            }

            return Unknown
        }

    }
}

val Bank.relatedBank: Bank?
    get() = when(this) {
        Bank.Ansar -> Bank.Sepah
        Bank.Bankino -> Bank.Khavarmianeh
        Bank.BluBank -> Bank.Saman
        Bank.HiBank -> Bank.Karafarin
        Bank.Karafarin -> Bank.HiBank
        Bank.Khavarmianeh -> Bank.Bankino
        Bank.Melal -> Bank.MetaBank
        Bank.Melli -> Bank.Noor
        Bank.MetaBank -> Bank.Melal
        Bank.Noor -> Bank.Melli
        Bank.Pasargad -> Bank.Wepod
        Bank.Saman -> Bank.BluBank
        Bank.Sepah -> Bank.Ansar
        Bank.Wepod -> Bank.Pasargad
        else -> null
    }
