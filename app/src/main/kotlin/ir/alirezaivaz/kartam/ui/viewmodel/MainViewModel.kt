package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.KartamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _db by lazy { KartamDatabase.instance }
    private val _cardDao by lazy { _db.cardDao() }

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _ownedCards = MutableStateFlow(emptyList<CardInfo>())
    val ownedCards: StateFlow<List<CardInfo>> = _ownedCards
    private val _othersCards = MutableStateFlow(emptyList<CardInfo>())
    val othersCards: StateFlow<List<CardInfo>> = _othersCards

    init {
        viewModelScope.launch {
            loadCards()
        }
    }

    fun updateIsLoading(value: Boolean) {
        _isLoading.value = value
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
            updateIsLoading(true)
        }
        BackupManager.restoreIfNeeded()
        val cards = withContext(Dispatchers.IO) {
            _cardDao.getAll()
        }
        updateCards(cards)
        if (isRefreshing) {
            updateIsRefreshing(false)
        } else {
            updateIsLoading(false)
        }
    }

    fun onMove(from: Int, to: Int, isOwned: Boolean) {
        val currentList = if (isOwned) {
            _ownedCards.value
        } else {
            _othersCards.value
        }.toMutableList()
        try {
            val item = currentList.removeAt(from)
            currentList.add(to, item)
            viewModelScope.launch {
                val updatedList = currentList.mapIndexed { index, card ->
                    if (card.position != index) card.copy(position = index) else card
                }
                withContext(Dispatchers.IO) {
                    updatedList.forEach { card ->
                        _cardDao.update(card)
                    }
                }
                if (isOwned) {
                    _ownedCards.value = updatedList
                } else {
                    _othersCards.value = updatedList
                }
                BackupManager.backupNow()
            }
        } catch (_: Exception) {
            return
        }
    }

    suspend fun deleteCard(card: CardInfo) {
        updateIsRefreshing(true)
        withContext(Dispatchers.IO) {
            _cardDao.delete(card)
        }
        BackupManager.backupNow()
        updateIsRefreshing(false)
    }
}
