package ir.alirezaivaz.kartam.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import ir.alirezaivaz.kartam.R

enum class Bank(
    val type: BankType = BankType.Bank,
    @param:DrawableRes
    val icon: Int,
    @param:DrawableRes
    val logo: Int,
    @param:StringRes
    val title: Int,
    val isNeo: Boolean = false,
    val isMerged: Boolean = false,
    val prefixes: List<String> = emptyList(),
    val nonPrefixes: List<String> = emptyList(),
    val supportedAccountTypes: List<AccountType> = AccountType.entries
) {
    @SerializedName("ABank")
    ABank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_abank,
        logo = R.drawable.ic_bank_abank_logo,
        title = R.string.bank_abank,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("Baam")
    Baam(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_baam,
        logo = R.drawable.ic_bank_baam,
        title = R.string.bank_baam,
        isNeo = true,
    ),
    @SerializedName("Banket")
    Banket(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_banket,
        logo = R.drawable.ic_bank_banket,
        title = R.string.bank_banket,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("Bankino")
    Bankino(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_bankino,
        logo = R.drawable.ic_bank_bankino_logo,
        title = R.string.bank_bankino,
        isNeo = true,
        prefixes = listOf("58594710"),
        supportedAccountTypes = listOf(AccountType.ShortTermDeposit)
    ),
    @SerializedName("Bankvareh")
    Bankvareh(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_bankvareh,
        logo = R.drawable.ic_bank_bankvareh,
        title = R.string.bank_bankvareh,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("Bajet")
    Bajet(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_bajet,
        logo = R.drawable.ic_bank_bajet_logo,
        title = R.string.bank_bajet,
        isNeo = true,
    ),
    @SerializedName("Baran")
    Baran(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_baran,
        logo = R.drawable.ic_bank_baran_logo,
        title = R.string.bank_baran,
        isNeo = true,
    ),
    @SerializedName("BluBank")
    BluBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_blu,
        logo = R.drawable.ic_bank_blu_logo,
        title = R.string.bank_blu,
        isNeo = true,
        prefixes = listOf("62198618", "62198619"),
        supportedAccountTypes = listOf(AccountType.ShortTermDeposit)
    ),
    @SerializedName("BluJunior")
    BluJunior(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_blu_jr,
        logo = R.drawable.ic_bank_blu_jr_logo,
        title = R.string.bank_blu_jr,
        isNeo = true,
        supportedAccountTypes = listOf(AccountType.ShortTermDeposit)
    ),
    @SerializedName("HiBank")
    HiBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_hibank,
        logo = R.drawable.ic_bank_hibank_logo,
        title = R.string.bank_hibank,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("MegaBank")
    MegaBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_megabank,
        logo = R.drawable.ic_bank_megabank,
        title = R.string.bank_megabank,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("MetaBank")
    MetaBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_metabank,
        logo = R.drawable.ic_bank_metabank_logo,
        title = R.string.bank_metabank,
        isNeo = true,
        prefixes = listOf(),
    ),
    @SerializedName("OmidBank")
    OmidBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_omid,
        logo = R.drawable.ic_bank_omid_logo,
        title = R.string.bank_omidbank,
        isNeo = true
    ),
    @SerializedName("QBank")
    QBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_qbank,
        logo = R.drawable.ic_bank_qbank_logo,
        title = R.string.bank_qbank,
        isNeo = true,
    ),
    @SerializedName("Sepino")
    Sepino(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_sepino,
        logo = R.drawable.ic_bank_sepino_logo,
        title = R.string.bank_sepino,
        isNeo = true,
    ),
    @SerializedName("Sibank")
    Sibank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_sibank,
        logo = R.drawable.ic_bank_sibank,
        title = R.string.bank_sibank,
        isNeo = true,
    ),
    @SerializedName("ToBank")
    ToBank(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_tobank,
        logo = R.drawable.ic_bank_tobank_logo,
        title = R.string.bank_tobank,
        isNeo = true,
    ),
    @SerializedName("Wepod")
    Wepod(
        type = BankType.NeoBank,
        icon = R.drawable.ic_bank_wepod,
        logo = R.drawable.ic_bank_wepod_logo,
        title = R.string.bank_wepod,
        isNeo = true,
        prefixes = listOf("50222915"),
    ),
    @SerializedName("Ansar")
    Ansar(
        icon = R.drawable.ic_bank_ansar,
        logo = R.drawable.ic_bank_ansar,
        title = R.string.bank_ansar,
        isMerged = true
    ),
    @SerializedName("Ayandeh")
    Ayandeh(
        icon = R.drawable.ic_bank_ayandeh,
        logo = R.drawable.ic_bank_ayandeh_logo,
        title = R.string.bank_ayandeh,
        isMerged = true
    ),
    @SerializedName("Central")
    Central(
        icon = R.drawable.ic_bank_central,
        logo = R.drawable.ic_bank_central_logo,
        title = R.string.bank_central,
        prefixes = listOf("636795")
    ),
    @SerializedName("Day")
    Day(
        icon = R.drawable.ic_bank_day,
        logo = R.drawable.ic_bank_day_logo,
        title = R.string.bank_day,
        prefixes = listOf("502938")
    ),
    @SerializedName("EghtesadNovin")
    EghtesadNovin(
        icon = R.drawable.ic_bank_eghtesad_novin,
        logo = R.drawable.ic_bank_eghtesad_novin_logo,
        title = R.string.bank_eghtesad_novin,
        prefixes = listOf("627412")
    ),
    @SerializedName("Gardeshgari")
    Gardeshgari(
        icon = R.drawable.ic_bank_gardeshgari,
        logo = R.drawable.ic_bank_gardeshgari_logo,
        title = R.string.bank_gardeshgari,
        prefixes = listOf("505416")
    ),
    @SerializedName("IranVenezuelaBiNationalBank")
    IranVenezuelaBiNationalBank(
        icon = R.drawable.ic_bank_iran_venezuela,
        logo = R.drawable.ic_bank_iran_venezuela_logo,
        title = R.string.bank_iran_venezuela,
        prefixes = listOf("581874")
    ),
    @SerializedName("IranZamin")
    IranZamin(
        icon = R.drawable.ic_bank_iranzamin,
        logo = R.drawable.ic_bank_iranzamin_logo,
        title = R.string.bank_iranzamin,
        prefixes = listOf("505785")
    ),
    @SerializedName("Karafarin")
    Karafarin(
        icon = R.drawable.ic_bank_karafarin,
        logo = R.drawable.ic_bank_karafarin_logo,
        title = R.string.bank_karafarin,
        prefixes = listOf("502910", "627488")
    ),
    @SerializedName("Keshavarzi")
    Keshavarzi(
        icon = R.drawable.ic_bank_keshavarzi,
        logo = R.drawable.ic_bank_keshavarzi_logo,
        title = R.string.bank_keshavarzi,
        prefixes = listOf("603770", "639217")
    ),
    @SerializedName("Khavarmianeh")
    Khavarmianeh(
        icon = R.drawable.ic_bank_khavarmianeh,
        logo = R.drawable.ic_bank_khavarmianeh_logo,
        title = R.string.bank_khavarmianeh,
        prefixes = listOf("585947"),
        nonPrefixes = Bankino.prefixes
    ),
    @SerializedName("Maskan")
    Maskan(
        icon = R.drawable.ic_bank_maskan,
        logo = R.drawable.ic_bank_maskan_logo,
        title = R.string.bank_maskan,
        prefixes = listOf("628023")
    ),
    @SerializedName("Mehr")
    Mehr(
        icon = R.drawable.ic_bank_mehr,
        logo = R.drawable.ic_bank_mehr_logo,
        title = R.string.bank_mehr,
        prefixes = listOf("606373")
    ),
    @SerializedName("Melal")
    Melal(
        type = BankType.CreditInstitution,
        icon = R.drawable.ic_bank_melal,
        logo = R.drawable.ic_bank_melal_logo,
        title = R.string.bank_melal,
        prefixes = listOf("606256")
    ),
    @SerializedName("Mellat")
    Mellat(
        icon = R.drawable.ic_bank_mellat,
        logo = R.drawable.ic_bank_mellat_logo,
        title = R.string.bank_mellat,
        prefixes = listOf("610433", "991975")
    ),
    @SerializedName("Melli")
    Melli(
        icon = R.drawable.ic_bank_melli,
        logo = R.drawable.ic_bank_melli_logo,
        title = R.string.bank_melli,
        prefixes = listOf("603799", "507677", "636214")
    ),
    @SerializedName("Noor")
    Noor(
        type = BankType.CreditInstitution,
        icon = R.drawable.ic_bank_noor,
        logo = R.drawable.ic_bank_noor_logo,
        title = R.string.bank_noor,
        isMerged = true
    ),
    @SerializedName("Parsian")
    Parsian(
        icon = R.drawable.ic_bank_parsian,
        logo = R.drawable.ic_bank_parsian_logo,
        title = R.string.bank_parsian,
        prefixes = listOf("622106", "627884", "639194")
    ),
    @SerializedName("Pasargad")
    Pasargad(
        icon = R.drawable.ic_bank_pasargad,
        logo = R.drawable.ic_bank_pasargad_logo,
        title = R.string.bank_pasargad,
        prefixes = listOf("502229", "639347"),
        nonPrefixes = Wepod.prefixes
    ),
    @SerializedName("PostBank")
    PostBank(
        icon = R.drawable.ic_bank_postbank,
        logo = R.drawable.ic_bank_postbank_logo,
        title = R.string.bank_postbank,
        prefixes = listOf("627760")
    ),
    @SerializedName("Refah")
    Refah(
        icon = R.drawable.ic_bank_refah,
        logo = R.drawable.ic_bank_refah_logo,
        title = R.string.bank_refah,
        prefixes = listOf("589463")
    ),
    @SerializedName("Resalat")
    Resalat(
        icon = R.drawable.ic_bank_resalat_icon,
        logo = R.drawable.ic_bank_resalat_logo,
        title = R.string.bank_resalat,
        prefixes = listOf("504172")
    ),
    @SerializedName("Saderat")
    Saderat(
        icon = R.drawable.ic_bank_saderat,
        logo = R.drawable.ic_bank_saderat_logo,
        title = R.string.bank_saderat,
        prefixes = listOf("603769"),
    ),
    @SerializedName("Saman")
    Saman(
        icon = R.drawable.ic_bank_saman,
        logo = R.drawable.ic_bank_saman_logo,
        title = R.string.bank_saman,
        prefixes = listOf("621986"),
        nonPrefixes = BluBank.prefixes
    ),
    @SerializedName("SanatVaMaadan")
    SanatVaMaadan(
        icon = R.drawable.ic_bank_sanat_maadan,
        logo = R.drawable.ic_bank_sanat_maadan_logo,
        title = R.string.bank_sanat_maadan,
        prefixes = listOf("627961")
    ),
    @SerializedName("Sarmayeh")
    Sarmayeh(
        icon = R.drawable.ic_bank_sarmayeh,
        logo = R.drawable.ic_bank_sarmayeh_logo,
        title = R.string.bank_sarmayeh,
        prefixes = listOf("639607")
    ),
    @SerializedName("Sepah")
    Sepah(
        icon = R.drawable.ic_bank_sepah,
        logo = R.drawable.ic_bank_sepah_logo,
        title = R.string.bank_sepah,
        prefixes = listOf("589210", "627381", "636949", "639599", "639370")
    ),
    @SerializedName("Shahr")
    Shahr(
        icon = R.drawable.ic_bank_shahr,
        logo = R.drawable.ic_bank_shahr_logo,
        title = R.string.bank_shahr,
        prefixes = listOf("502806", "504706")
    ),
    @SerializedName("Sina")
    Sina(
        icon = R.drawable.ic_bank_sina,
        logo = R.drawable.ic_bank_sina_logo,
        title = R.string.bank_sina,
        prefixes = listOf("639346")
    ),
    @SerializedName("Tejarat")
    Tejarat(
        icon = R.drawable.ic_bank_tejarat,
        logo = R.drawable.ic_bank_tejarat_logo,
        title = R.string.bank_tejarat,
        prefixes = listOf("585983", "627353")
    ),
    @SerializedName("ToseSaderat")
    ToseSaderat(
        icon = R.drawable.ic_bank_tose_saderat,
        logo = R.drawable.ic_bank_tose_saderat_logo,
        title = R.string.bank_tose_saderat,
        prefixes = listOf("627648")
    ),
    @SerializedName("ToseTaavon")
    ToseTaavon(
        icon = R.drawable.ic_bank_tose_taavon,
        logo = R.drawable.ic_bank_tose_taavon_logo,
        title = R.string.bank_tose_taavon,
        prefixes = listOf("502908")
    ),
    @SerializedName("Unknown")
    Unknown(
        icon = R.drawable.ic_bank_unknown,
        logo = R.drawable.ic_bank_unknown,
        title = R.string.bank_unknown,
    );

    companion object {
        private val allBanks = entries.filter { it != Unknown }

        fun getBank(bank: String): Bank {
            return runCatching {
                valueOf(bank)
            }.getOrElse {
                Unknown
            }
        }

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

val Bank.parentBank: Bank?
    get() = when (this) {
        Bank.BluBank, Bank.BluJunior -> Bank.Saman
        Bank.Bankino -> Bank.Khavarmianeh
        Bank.Bankvareh -> Bank.ToseTaavon
        Bank.Baran -> Bank.Keshavarzi
        Bank.Bajet -> Bank.Tejarat
        Bank.Wepod -> Bank.Pasargad
        Bank.MegaBank -> Bank.Mellat
        Bank.MetaBank -> Bank.Melal
        Bank.HiBank -> Bank.Karafarin
        Bank.Baam, Bank.Ayandeh, Bank.ABank, Bank.Noor -> Bank.Melli
        Bank.OmidBank, Bank.Ansar -> Bank.Sepah
        Bank.QBank, Bank.Banket -> Bank.Mehr
        Bank.Sepino -> Bank.Saderat
        Bank.Sibank -> Bank.Sina
        Bank.ToBank -> Bank.Gardeshgari
        else -> null
    }

val Bank.childBanks: List<Bank>
    get() = when (this) {
        Bank.Saman -> listOf(Bank.BluBank, Bank.BluJunior)
        Bank.Khavarmianeh -> listOf(Bank.Bankino)
        Bank.Pasargad -> listOf(Bank.Wepod)
        Bank.Melal -> listOf(Bank.MetaBank)
        Bank.Gardeshgari -> listOf(Bank.ToBank)
        Bank.Karafarin -> listOf(Bank.HiBank)
        Bank.Keshavarzi -> listOf(Bank.Baran)
        Bank.Mehr -> listOf(Bank.QBank, Bank.Banket)
        Bank.Mellat -> listOf(Bank.MegaBank)
        Bank.Melli -> listOf(Bank.Baam, Bank.Ayandeh, Bank.ABank, Bank.Noor)
        Bank.Saderat -> listOf(Bank.Sepino)
        Bank.Sepah -> listOf(Bank.OmidBank, Bank.Ansar)
        Bank.Sina -> listOf(Bank.Sibank)
        Bank.Tejarat -> listOf(Bank.Bajet)
        Bank.ToseTaavon -> listOf(Bank.Bankvareh)
        else -> emptyList()
    }

fun Bank.getChoosableBanks(): List<Bank> {
    if (this == Bank.Unknown) return emptyList()
    val result = mutableListOf<Bank>()
    if (parentBank != null && parentBank!!.childBanks.isNotEmpty()) {
        result.add(parentBank!!)
        result.addAll(parentBank!!.childBanks)
    } else if (childBanks.isNotEmpty()) {
        result.add(this)
        result.addAll(childBanks)
    }
    // Avoid duplicates and self-duplicates
    return result.distinct()
}
