package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.ErrorCode
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.dto.Result
import ir.alirezaivaz.kartam.extensions.formattedMonth
import ir.alirezaivaz.kartam.extensions.formattedYear
import ir.alirezaivaz.kartam.extensions.isValidAccountNumber
import ir.alirezaivaz.kartam.extensions.isValidCardNumber
import ir.alirezaivaz.kartam.extensions.isValidCvv2
import ir.alirezaivaz.kartam.extensions.isValidMonth
import ir.alirezaivaz.kartam.extensions.isValidName
import ir.alirezaivaz.kartam.extensions.isValidShabaNumber
import ir.alirezaivaz.kartam.extensions.isValidYear
import ir.alirezaivaz.kartam.utils.AppDatabase
import ir.alirezaivaz.kartam.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCardViewModel(
    db: AppDatabase,
    private val cardId: Int
) : ViewModel() {
    private val _cardDao = db.cardDao()

    private val _card = MutableStateFlow<CardInfo?>(null)
    val card: StateFlow<CardInfo?> = _card
    private val _isEdit = MutableStateFlow(false)
    val isEdit: StateFlow<Boolean> = _isEdit
    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadCardDetails()
        }
    }

    suspend fun loadCardDetails() {
        _loadingState.value = LoadingState.LOADING
        if (cardId == -1) {
            _isEdit.value = false
            _loadingState.value = LoadingState.LOADED
        } else {
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
                    val cvv2 = Utils.getCvv2(currentCard.cvv2.toString(), true)
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
                _loadingState.value = LoadingState.LOADED
            } else {
                _loadingState.value = LoadingState.EMPTY
            }
        }
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

    fun validateFields(): Result {
        if (_cardNumber.value.text.isEmpty()) {
            return Result(errorCode = ErrorCode.EmptyCardNumber)
        } else if (!_cardNumber.value.text.isValidCardNumber()) {
            return Result(errorCode = ErrorCode.InvalidCardNumber)
        } else if (_ownerName.value.text.isEmpty()) {
            return Result(errorCode = ErrorCode.EmptyCardOwnerName)
        } else if (!_ownerName.value.text.isValidName()) {
            return Result(errorCode = ErrorCode.InvalidCardOwnerName)
        } else if (_ownerEnglishName.value.text.isNotEmpty() && !_ownerEnglishName.value.text.isValidName()) {
            return Result(errorCode = ErrorCode.InvalidCardOwnerName)
        } else if (_shabaNumber.value.text.isNotEmpty() && !_shabaNumber.value.text.isValidShabaNumber()) {
            return Result(errorCode = ErrorCode.InvalidShabaNumber)
        } else if (_accountNumber.value.text.isNotEmpty() && !_accountNumber.value.text.isValidAccountNumber()) {
            return Result(errorCode = ErrorCode.InvalidAccountNumber)
        } else if (_cvv2.value.text.isNotEmpty() && !_cvv2.value.text.isValidCvv2()) {
            return Result(errorCode = ErrorCode.InvalidCvv2)
        } else if (_expirationMonth.value.text.isNotEmpty() && !_expirationMonth.value.text.isValidMonth()) {
            return Result(errorCode = ErrorCode.InvalidExpirationMonth)
        } else if (_expirationYear.value.text.isNotEmpty() && !_expirationYear.value.text.isValidYear()) {
            return Result(errorCode = ErrorCode.InvalidExpirationYear)
        }
        return Result(isSuccess = true)
    }

    suspend fun addCard(): Result {
        val isAllFieldsValid = validateFields()
        if (isAllFieldsValid.isSuccess) {
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
                    cvv2 = _cvv2.value.text.toIntOrNull(),
                    bank = _bank.value,
                    position = position + 1
                )
                _cardDao.insert(card)
            }
            return Result(isSuccess = true)
        } else {
            return isAllFieldsValid
        }
    }

    suspend fun updateCard(): Result {
        val isAllFieldsValid = validateFields()
        if (_card.value != null && isAllFieldsValid.isSuccess) {
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
                    cvv2 = _cvv2.value.text.toIntOrNull(),
                    bank = _bank.value
                )
                _cardDao.update(card)
            }
            return Result(isSuccess = true)
        } else {
            return isAllFieldsValid
        }
    }

}
