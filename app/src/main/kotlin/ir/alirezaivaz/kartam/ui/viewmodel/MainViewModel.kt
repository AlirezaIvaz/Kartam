package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.utils.KartamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(db: KartamDatabase) : ViewModel() {
    private val _cardDao = db.cardDao()

    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _ownedCards = MutableStateFlow(emptyList<CardInfo>())
    val ownedCards: StateFlow<List<CardInfo>> = _ownedCards
    private val _othersCards = MutableStateFlow(emptyList<CardInfo>())
    val othersCards: StateFlow<List<CardInfo>> = _othersCards

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
        _ownedCards.value = cards.filter { it.isOwned }
        _othersCards.value = cards.filter { !it.isOwned }
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

    fun onMove(from: Int, to: Int, isOwned: Boolean) {
        val currentList = if (isOwned) {
            _ownedCards.value
        } else {
            _othersCards.value
        }.toMutableList()
        try {
            val item = currentList.removeAt(from - 1)
            currentList.add(to - 1, item)
            viewModelScope.launch {
                currentList.forEachIndexed { index, card ->
                    if (card.position != index) {
                        _cardDao.update(card.copy(position = index))
                    }
                }
                if (isOwned) {
                    _ownedCards.value = currentList
                } else {
                    _othersCards.value = currentList
                }
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

        fun getInstance(db: KartamDatabase): MainViewModel {
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
