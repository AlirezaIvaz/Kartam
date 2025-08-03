package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.utils.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(db: AppDatabase) : ViewModel() {
    private val _cardDao = db.cardDao()

    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _cards = MutableStateFlow(emptyList<CardInfo>())
    val cards: StateFlow<List<CardInfo>> = _cards

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadCards()
        }
    }

    fun updateLoadingState(loadingState: LoadingState) {
        _loadingState.value = loadingState
    }

    fun updateIsRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    fun updateCards(cards: List<CardInfo>) {
        _cards.value = cards
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

    suspend fun deleteCard(card: CardInfo) {
        updateIsRefreshing(true)
        _cardDao.delete(card)
        updateIsRefreshing(false)
    }

    companion object {
        private var _instance: MainViewModel? = null

        fun getInstance(db: AppDatabase): MainViewModel {
            if (_instance == null) {
                _instance = MainViewModel(db)
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
