package ir.alirezaivaz.kartam.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.dto.CardItem
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.dto.toSensitive
import ir.alirezaivaz.kartam.utils.AppDatabase
import ir.alirezaivaz.kartam.utils.KartamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
    val context: Context, // TODO: Remove when migration completed
    val db: KartamDatabase,
    val oldDb: AppDatabase // TODO: Remove when migration completed
) : ViewModel() {
    private val _cardDao = db.cardDao()
    private val _oldCardDao = oldDb.cardDao()

    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _cards = MutableStateFlow(emptyList<CardItem>())
    val cards: StateFlow<List<CardItem>> = _cards

    init {
        viewModelScope.launch(Dispatchers.IO) {
            migrateIfNeeded(context)
            loadCards()
        }
    }

    fun updateLoadingState(loadingState: LoadingState) {
        _loadingState.value = loadingState
    }

    fun updateIsRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    fun updateCards(cards: List<CardItem>) {
        _cards.value = cards
    }

    suspend fun migrateIfNeeded(context: Context) {
        val encryptedFile = AppDatabase.getDatabaseFile(context)
        val newFile = KartamDatabase.getDatabaseFile(context)
        if (encryptedFile.exists() && !newFile.exists()) {
            val oldCards = _oldCardDao.getAll().map {
                CardItem(
                    id = it.id,
                    name = it.name,
                    englishName = it.englishName?.ifBlank { null },
                    number = it.number,
                    shabaNumber = it.shabaNumber?.ifBlank { null },
                    accountNumber = it.accountNumber?.ifBlank { null },
                    branchCode = it.branchCode,
                    branchName = it.branchName?.ifBlank { null },
                    expirationMonth = it.expirationMonth,
                    expirationYear = it.expirationYear,
                    cvv2 = it.cvv2?.toString().toSensitive(),
                    bank = it.bank,
                    position = it.position
                )
            }
            db.runInTransaction {
                runBlocking {
                    _cardDao.insertAll(oldCards)
                }
            }
            encryptedFile.delete()
        }
    }

    suspend fun loadCards(isRefreshing: Boolean = false) {
        if (isRefreshing) {
            updateIsRefreshing(true)
        } else {
            updateLoadingState(LoadingState.LOADING)
        }
        val cards = _cardDao.getAll()
        updateCards(cards)
        delay(500)
        if (isRefreshing) {
            updateIsRefreshing(false)
        } else {
            val state = if (cards.isEmpty()) {
                LoadingState.EMPTY
            } else {
                LoadingState.LOADED
            }
            updateLoadingState(state)
        }
    }

    fun onMove(from: Int, to: Int) {
        val currentList = _cards.value.toMutableList()
        try {
            val item = currentList.removeAt(from -1)
            currentList.add(to -1, item)
            viewModelScope.launch {
                currentList.forEachIndexed { index, card ->
                    if (card.position != index) {
                        _cardDao.update(card.copy(position = index))
                    }
                }
                _cards.value = currentList
            }
        } catch (_: Exception) {
            return
        }
    }

    suspend fun deleteCard(card: CardItem) {
        updateIsRefreshing(true)
        _cardDao.delete(card)
        updateIsRefreshing(false)
    }

    companion object {
        private var _instance: MainViewModel? = null

        fun getInstance(context: Context, db: KartamDatabase, oldDb: AppDatabase): MainViewModel {
            if (_instance == null) {
                _instance = MainViewModel(context, db, oldDb)
            }
            return _instance!!
        }

        fun disposeInstance() {
            if (_instance != null) {
                _instance = null
            }
        }
    }
}
