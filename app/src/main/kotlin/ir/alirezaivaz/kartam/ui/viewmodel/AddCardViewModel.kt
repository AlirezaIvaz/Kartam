package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.Result
import ir.alirezaivaz.kartam.dto.toSensitive
import ir.alirezaivaz.kartam.dto.toStringOrNull
import ir.alirezaivaz.kartam.extensions.formattedMonth
import ir.alirezaivaz.kartam.extensions.isValidAccountNumber
import ir.alirezaivaz.kartam.extensions.isValidCardNumber
import ir.alirezaivaz.kartam.extensions.isValidCvv2
import ir.alirezaivaz.kartam.extensions.isValidMonth
import ir.alirezaivaz.kartam.extensions.isValidName
import ir.alirezaivaz.kartam.extensions.isValidShabaNumber
import ir.alirezaivaz.kartam.extensions.isValidYear
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.alirezaivaz.kartam.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCardViewModel(
    db: KartamDatabase,
    private val cardId: Int,
    private val isOwned: Boolean,
) : ViewModel() {
    private val _cardDao = db.cardDao()

    private val _card = MutableStateFlow<CardInfo?>(null)
    val card: StateFlow<CardInfo?> = _card
    private val _isEdit = MutableStateFlow(false)
    val isEdit: StateFlow<Boolean> = _isEdit
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _result = MutableStateFlow<Result?>(null)
    val result: StateFlow<Result?> = _result
    private val _cardNumber = MutableStateFlow(TextFieldValue())
    val cardNumber: StateFlow<TextFieldValue> = _cardNumber
    private val _ownerName = MutableStateFlow(TextFieldValue())
    val ownerName: StateFlow<TextFieldValue> = _ownerName
    private val _ownerEnglishName = MutableStateFlow(TextFieldValue())
    val ownerEnglishName: StateFlow<TextFieldValue> = _ownerEnglishName
    private val _bank = MutableStateFlow(Bank.Unknown)
    val bank: StateFlow<Bank> = _bank
    private val _shabaNumber = MutableStateFlow(TextFieldValue())
    val shabaNumber: StateFlow<TextFieldValue> = _shabaNumber
    private val _accountNumber = MutableStateFlow(TextFieldValue())
    val accountNumber: StateFlow<TextFieldValue> = _accountNumber
    private val _branchCode = MutableStateFlow(TextFieldValue())
    val branchCode: StateFlow<TextFieldValue> = _branchCode
    private val _branchName = MutableStateFlow(TextFieldValue())
    val branchName: StateFlow<TextFieldValue> = _branchName
    private val _expirationMonth = MutableStateFlow(TextFieldValue())
    val expirationMonth: StateFlow<TextFieldValue> = _expirationMonth
    private val _expirationYear = MutableStateFlow(TextFieldValue())
    val expirationYear: StateFlow<TextFieldValue> = _expirationYear
    private val _cvv2 = MutableStateFlow(TextFieldValue())
    val cvv2: StateFlow<TextFieldValue> = _cvv2
    private val _isOthersCard = MutableStateFlow(!isOwned)
    val isOthersCard: StateFlow<Boolean> = _isOthersCard

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (cardId == -1) {
                _isEdit.value = false
                updateIsLoading(false)
            } else {
                loadCardDetails()
            }
        }
    }

    suspend fun loadCardDetails() {
        _isEdit.value = true
        _card.value = _cardDao.getCard(cardId)
        if (_card.value != null) {
            val currentCard = _card.value!!
            updateCardNumber(TextFieldValue(currentCard.number))
            updateOwnerName(TextFieldValue(currentCard.name))
            if (!currentCard.englishName.isNullOrEmpty()) {
                updateOwnerEnglishName(TextFieldValue(currentCard.englishName))
            }
            if (!currentCard.shabaNumber.isNullOrEmpty()) {
                updateShabaNumber(TextFieldValue(currentCard.shabaNumber))
            }
            if (!currentCard.accountNumber.isNullOrEmpty()) {
                updateAccountNumber(TextFieldValue(currentCard.accountNumber))
            }
            if (currentCard.cvv2 != null) {
                val cvv2 = Utils.getCvv2(currentCard.cvv2.toStringOrNull(), true)
                updateCvv2(TextFieldValue(cvv2))
            }
            if (currentCard.branchCode != null) {
                updateBranchCode(TextFieldValue(currentCard.branchCode.toString()))
            }
            if (currentCard.branchName != null) {
                updateBranchName(TextFieldValue(currentCard.branchName))
            }
            if (currentCard.expirationMonth != null) {
                updateExpirationMonth(TextFieldValue(currentCard.expirationMonth.formattedMonth()))
            }
            if (currentCard.expirationYear != null) {
                // Year was saved as a 2-digit number previously
                updateExpirationYear(TextFieldValue(currentCard.expirationYear.formattedMonth()))
            }
            updateIsOthersCard(!currentCard.isOwned)
        } else {
            updateResult(
                isSuccess = false,
                message = R.string.message_went_wrong_description
            )
        }
        delay(5000)
        updateIsLoading(false)
    }

    fun updateIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun updateResult(result: Result?) {
        _result.value = result
    }

    fun updateResult(
        isSuccess: Boolean = false,
        @StringRes
        message: Int? = null,
    ) {
        _result.value = Result(
            isSuccess = isSuccess,
            message = message,
        )
    }

    fun updateCardNumber(cardNumber: TextFieldValue) {
        _cardNumber.value = cardNumber
        updateBank(cardNumber.text)
    }

    fun updateOwnerName(ownerName: TextFieldValue) {
        _ownerName.value = ownerName
    }

    fun updateOwnerEnglishName(ownerEnglishName: TextFieldValue) {
        _ownerEnglishName.value = ownerEnglishName
    }

    fun updateBank(cardNumber: String) {
        _bank.value = Bank.fromCardNumber(cardNumber)
        if (_bank.value.isNeo) {
            updateBranchCode(TextFieldValue())
            updateBranchName(TextFieldValue())
        }
    }

    fun updateBank(bank: Bank) {
        _bank.value = bank
        if (_bank.value.isNeo) {
            updateBranchCode(TextFieldValue())
            updateBranchName(TextFieldValue())
        }
    }

    fun updateShabaNumber(shabaNumber: TextFieldValue) {
        _shabaNumber.value = shabaNumber
    }

    fun updateAccountNumber(accountNumber: TextFieldValue) {
        _accountNumber.value = accountNumber
    }

    fun updateBranchCode(branchCode: TextFieldValue) {
        _branchCode.value = branchCode
    }

    fun updateBranchName(branchName: TextFieldValue) {
        _branchName.value = branchName
    }

    fun updateExpirationMonth(expirationMonth: TextFieldValue) {
        _expirationMonth.value = expirationMonth
    }

    fun updateExpirationYear(expirationYear: TextFieldValue) {
        _expirationYear.value = expirationYear
    }

    fun updateCvv2(cvv2: TextFieldValue) {
        _cvv2.value = cvv2
    }

    fun updateIsOthersCard(value: Boolean) {
        _isOthersCard.value = value
    }

    fun validateFields(): Boolean {
        if (_cardNumber.value.text.isEmpty()) {
            updateResult(message = R.string.error_empty_card_number)
            return false
        } else if (!_cardNumber.value.text.isValidCardNumber()) {
            updateResult(message = R.string.error_invalid_card_number)
            return false
        } else if (_ownerName.value.text.isEmpty()) {
            updateResult(message = R.string.error_empty_owner_name)
            return false
        } else if (!_ownerName.value.text.isValidName()) {
            updateResult(message = R.string.error_invalid_owner_name)
            return false
        } else if (_ownerEnglishName.value.text.isNotEmpty() && !_ownerEnglishName.value.text.isValidName()) {
            updateResult(message = R.string.error_invalid_owner_name)
            return false
        } else if (_shabaNumber.value.text.isNotEmpty() && !_shabaNumber.value.text.isValidShabaNumber()) {
            updateResult(message = R.string.error_invalid_shaba_number)
            return false
        } else if (_accountNumber.value.text.isNotEmpty() && !_accountNumber.value.text.isValidAccountNumber()) {
            updateResult(message = R.string.error_invalid_account_number)
            return false
        } else if (_cvv2.value.text.isNotEmpty() && !_cvv2.value.text.isValidCvv2()) {
            updateResult(message = R.string.error_invalid_cvv2)
            return false
        } else if (_expirationMonth.value.text.isNotEmpty() && !_expirationMonth.value.text.isValidMonth()) {
            updateResult(message = R.string.error_invalid_exp_month)
            return false
        } else if (_expirationYear.value.text.isNotEmpty() && !_expirationYear.value.text.isValidYear()) {
            updateResult(message = R.string.error_invalid_exp_year)
            return false
        }
        return true
    }

    suspend fun addCard() {
        val isAllFieldsValid = validateFields()
        if (isAllFieldsValid) {
            updateIsLoading(true)
            withContext(Dispatchers.IO) {
                val position = _cardDao.getMaxPosition() ?: 0
                val card = CardInfo(
                    name = _ownerName.value.text,
                    englishName = _ownerEnglishName.value.text.ifBlank { null },
                    number = _cardNumber.value.text,
                    shabaNumber = _shabaNumber.value.text.ifBlank { null },
                    accountNumber = _accountNumber.value.text.ifBlank { null },
                    branchCode = _branchCode.value.text.toIntOrNull(),
                    branchName = _branchName.value.text.ifBlank { null },
                    expirationMonth = _expirationMonth.value.text.toIntOrNull(),
                    expirationYear = _expirationYear.value.text.toIntOrNull(),
                    cvv2 = _cvv2.value.text.toSensitive(),
                    bank = _bank.value,
                    isOwned = !_isOthersCard.value,
                    position = position + 1
                )
                _cardDao.insert(card)
            }
            updateIsLoading(false)
            updateResult(isSuccess = true)
        }
    }

    suspend fun updateCard() {
        val isAllFieldsValid = validateFields()
        if (_card.value != null && isAllFieldsValid) {
            updateIsLoading(true)
            withContext(Dispatchers.IO) {
                val card = _card.value!!.copy(
                    name = _ownerName.value.text,
                    englishName = _ownerEnglishName.value.text.ifBlank { null },
                    number = _cardNumber.value.text,
                    shabaNumber = _shabaNumber.value.text.ifBlank { null },
                    accountNumber = _accountNumber.value.text.ifBlank { null },
                    branchCode = _branchCode.value.text.toIntOrNull(),
                    branchName = _branchName.value.text.ifBlank { null },
                    expirationMonth = _expirationMonth.value.text.toIntOrNull(),
                    expirationYear = _expirationYear.value.text.toIntOrNull(),
                    cvv2 = _cvv2.value.text.toSensitive(),
                    bank = _bank.value,
                    isOwned = !_isOthersCard.value
                )
                _cardDao.update(card)
            }
            updateIsLoading(false)
            updateResult(
                isSuccess = true,
                message = R.string.message_card_updated
            )
        }
    }

}
