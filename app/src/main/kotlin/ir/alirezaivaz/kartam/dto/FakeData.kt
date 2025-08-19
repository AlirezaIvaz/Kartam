package ir.alirezaivaz.kartam.dto

@Suppress("MayBeConstant")
object FakeData {
    val name = "Alireza Ivaz"
    val numberBlu = "6219861915353212"
    val numberSaman = "6219861074845263"
    val numberSaderat = "6037697470429504"
    val numberBankino = "5859471024523213"
    val numberWepod = "5022291583909450"
    val shabaNumber = "IR123456789123456789123456"
    val accountNumber = "123456789123456789"
    val expirationMonth = 5
    val expirationYear = 6
    val cvv2 = 123
    val bank = Bank.fromCardNumber(numberBlu)

    val bluCard = CardItem(
        name = name,
        number = numberBlu,
        shabaNumber = shabaNumber,
        accountNumber = accountNumber,
        expirationMonth = expirationMonth,
        expirationYear = expirationYear,
        cvv2 = cvv2.toString().toSensitive(),
        bank = bank
    )
    val cards = listOf(
        bluCard,
        bluCard.copy(
            number = numberSaman,
            bank = Bank.fromCardNumber(numberSaman)
        ),
        bluCard.copy(
            number = numberSaderat,
            bank = Bank.fromCardNumber(numberSaderat),
            cvv2 = 2116.toString().toSensitive()
        ),
        bluCard.copy(
            number = numberBankino,
            bank = Bank.fromCardNumber(numberBankino)
        ),
        bluCard.copy(
            number = numberWepod,
            bank = Bank.fromCardNumber(numberWepod)
        ),
    )

}
