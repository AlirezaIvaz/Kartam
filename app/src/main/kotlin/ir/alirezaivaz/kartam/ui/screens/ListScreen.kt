package ir.alirezaivaz.kartam.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.ui.dialogs.DeleteCardDialog
import ir.alirezaivaz.kartam.ui.sheets.CardOptionsSheet
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.ErrorView
import ir.alirezaivaz.kartam.ui.widgets.list.CardList
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.BiometricHelper
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    cards: List<CardInfo>,
    isOwned: Boolean,
    toaster: ToasterState,
    viewModel: MainViewModel,
    onEditRequest: (id: Int?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val hapticFeedback = LocalHapticFeedback.current
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }
    var selectedCardSnapshot by remember { mutableStateOf<ImageBitmap?>(null) }
    var showCardOptionsSheet by remember { mutableStateOf(false) }
    var showDeleteCardDialog by remember { mutableStateOf(false) }
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
                                onEditRequest(selectedCard?.id)
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
                    onEditRequest(selectedCard?.id)
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
                scope.launch(Dispatchers.IO) {
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
            !state && cards.isEmpty() -> {
                ErrorView(
                    icon = painterResource(R.drawable.vector_credit_card),
                    title = stringResource(R.string.message_no_card),
                    description = stringResource(R.string.message_no_card_description),
                    actionButtonText = stringResource(R.string.action_reload),
                    actionButtonIcon = painterResource(R.drawable.ic_reload),
                    actionButtonPressed = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.loadCards()
                        }
                    }
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.loadCards(isRefreshing = true)
                        }
                    }
                ) {
                    CardList(
                        cards = cards.filter { it.isOwned == isOwned },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val db = KartamDatabase.getInstance(context)
    val backupManager by lazy { BackupManager.getInstance(context.noBackupFilesDir, db) }
    val viewModel = MainViewModel.getInstance(db, backupManager)
    ListScreen(
        cards = emptyList(),
        isOwned = true,
        viewModel = viewModel,
        toaster = rememberToasterState(),
        onEditRequest = {}
    )
}
