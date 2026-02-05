package ir.alirezaivaz.kartam.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.dialogs.DeleteCardDialog
import ir.alirezaivaz.kartam.ui.sheets.CardOptionsSheet
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.ErrorView
import ir.alirezaivaz.kartam.ui.widgets.KartamSearchBar
import ir.alirezaivaz.kartam.ui.widgets.list.CardList
import ir.alirezaivaz.kartam.utils.BiometricHelper
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ListScreen(
    cards: List<CardInfo>,
    isOwned: Boolean,
    toaster: ToasterState,
    viewModel: MainViewModel = viewModel(),
    onEditRequest: (launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, id: Int?) -> Unit,
    onAddCardClick: (launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val hapticFeedback = LocalHapticFeedback.current
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val activityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    viewModel.loadCards(isRefreshing = true)
                }
            }
        }
    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }
    var selectedCardSnapshot by remember { mutableStateOf<ImageBitmap?>(null) }
    var showCardOptionsSheet by remember { mutableStateOf(false) }
    var showDeleteCardDialog by remember { mutableStateOf(false) }
    val filteredCards = remember { mutableStateListOf<CardInfo>() }
    var searchFilter by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
    val authType by SettingsManager.authType.collectAsState()
    val isAuthSecretData by SettingsManager.isAuthSecretData.collectAsState()
    val isAuthOwnedCardDetails by SettingsManager.isAuthOwnedCardDetails.collectAsState()
    val isAuthBeforeEdit by SettingsManager.isAuthBeforeEdit.collectAsState()
    val isAuthBeforeDelete by SettingsManager.isAuthBeforeDelete.collectAsState()
    val isSecretCvv2InList by SettingsManager.isSecretCvv2InList.collectAsState()
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.onMove(from.index, to.index, isOwned)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            filteredCards.clear()
            if (searchFilter.isNotEmpty()) {
                val filtered = cards.filter { it.name.contains(searchFilter) }
                filteredCards.addAll(filtered)
            }
            isSearching = false
        }
    }

    if (showCardOptionsSheet) {
        CardOptionsSheet(
            card = selectedCard,
            onEditRequest = {
                if (isAuthBeforeEdit) {
                    BiometricHelper(
                        activity = activity as FragmentActivity,
                        authType = authType,
                        onResult = {
                            if (it) {
                                showCardOptionsSheet = false
                                onEditRequest(activityLauncher, selectedCard?.id)
                            } else {
                                toaster.show(
                                    message = resources.getString(R.string.error_authentication_failed),
                                    type = ToastType.Error
                                )
                            }
                        }
                    ).authenticate()
                } else {
                    showCardOptionsSheet = false
                    onEditRequest(activityLauncher, selectedCard?.id)
                }
            },
            onDeleteRequest = {
                if (isAuthBeforeDelete) {
                    BiometricHelper(
                        activity = activity as FragmentActivity,
                        authType = authType,
                        onResult = {
                            if (it) {
                                showCardOptionsSheet = false
                                showDeleteCardDialog = true
                            } else {
                                toaster.show(
                                    message = resources.getString(R.string.error_authentication_failed),
                                    type = ToastType.Error
                                )
                            }
                        }
                    ).authenticate()
                } else {
                    showCardOptionsSheet = false
                    showDeleteCardDialog = true
                }
            },
            onSnapshotReady = {
                selectedCardSnapshot = it
            },
            onDismissRequest = {
                selectedCard = null
                selectedCardSnapshot = null
                showCardOptionsSheet = false
            }
        )
    }
    if (showDeleteCardDialog) {
        DeleteCardDialog(
            snapshot = selectedCardSnapshot,
            onDeleteRequest = {
                showDeleteCardDialog = false
                showCardOptionsSheet = false
                scope.launch {
                    if (selectedCard != null) {
                        viewModel.deleteCard(selectedCard!!)
                        selectedCard = null
                        selectedCardSnapshot = null
                        toaster.show(
                            message = resources.getString(R.string.message_card_deleted),
                            type = ToastType.Success
                        )
                        viewModel.loadCards(isRefreshing = true)
                    } else {
                        toaster.show(
                            message = resources.getString(R.string.message_went_wrong),
                            type = ToastType.Error
                        )
                    }
                }
            },
            onDismissRequest = {
                showDeleteCardDialog = false
                selectedCard = null
                selectedCardSnapshot = null
            }
        )
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            KartamSearchBar(
                query = searchFilter,
                isOwned = isOwned,
                isExpanded = isSearchExpanded,
                isLoading = isSearching,
                searchResults = filteredCards,
                toaster = toaster,
                authType = authType,
                isCvv2VisibleByDefault = !isSecretCvv2InList,
                isAuthOwnedCardDetails = isAuthOwnedCardDetails,
                isAuthenticationRequired = isAuthSecretData,
                reorderableLazyListState = reorderableLazyListState,
                onSearch = {
                    isSearching = true
                },
                onQueryChange = {
                    searchFilter = it
                },
                onExpandChange = {
                    isSearchExpanded = it
                    if (!it) {
                        isSearching = false
                        searchFilter = ""
                        filteredCards.clear()
                    }
                },
                onCardSelect = {
                    selectedCard = it
                    showCardOptionsSheet = true
                },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isSearchExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.handPointerIcon(),
                    text = {
                        Text(
                            text = stringResource(R.string.action_add_card)
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = stringResource(R.string.action_add_card)
                        )
                    },
                    onClick = {
                        onAddCardClick(activityLauncher)
                    }
                )
            }
        },
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "CardListAnimation"
        ) { state ->
            when {
                state -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth()
                    )
                }

                !state && cards.isEmpty() -> {
                    ErrorView(
                        icon = painterResource(R.drawable.vector_credit_card),
                        title = stringResource(R.string.message_no_card),
                        modifier = Modifier.padding(it),
                        description = stringResource(R.string.message_no_card_description),
                        actionButtonText = stringResource(R.string.action_reload),
                        actionButtonIcon = painterResource(R.drawable.ic_reload),
                        actionButtonPressed = {
                            scope.launch {
                                viewModel.loadCards()
                            }
                        }
                    )
                }

                else -> {
                    PullToRefreshBox(
                        modifier = Modifier.padding(it),
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            scope.launch {
                                viewModel.loadCards(isRefreshing = true)
                            }
                        }
                    ) {
                        CardList(
                            cards = cards,
                            toaster = toaster,
                            authType = authType,
                            lazyListState = lazyListState,
                            reorderableLazyListState = reorderableLazyListState,
                            isCvv2VisibleByDefault = !isSecretCvv2InList,
                            isAuthOwnedCardDetails = isAuthOwnedCardDetails,
                            isAuthenticationRequired = isAuthSecretData,
                            onCardSelect = {
                                selectedCard = it
                                showCardOptionsSheet = true
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ListScreen(
        cards = emptyList(),
        isOwned = true,
        toaster = rememberToasterState(),
        onEditRequest = { _, _ -> },
        onAddCardClick = {}
    )
}
