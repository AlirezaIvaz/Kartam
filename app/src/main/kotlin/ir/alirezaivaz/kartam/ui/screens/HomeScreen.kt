package ir.alirezaivaz.kartam.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.ui.dialogs.DeleteCardDialog
import ir.alirezaivaz.kartam.ui.sheets.CardOptionsSheet
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.ErrorView
import ir.alirezaivaz.kartam.utils.KartamDatabase
import ir.alirezaivaz.kartam.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    toaster: ToasterState,
    viewModel: MainViewModel,
    onEditRequest: (id: Int?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val loadingState by viewModel.loadingState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val cards by viewModel.cards.collectAsState()
    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }
    var selectedCardSnapshot by remember { mutableStateOf<ImageBitmap?>(null) }
    var showCardOptionsSheet by remember { mutableStateOf(false) }
    var showDeleteCardDialog by remember { mutableStateOf(false) }
    val isSecretCvv2InList by SettingsManager.isSecretCvv2InList.collectAsState()
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.onMove(from.index, to.index)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    if (showCardOptionsSheet) {
        CardOptionsSheet(
            card = selectedCard,
            onEditRequest = {
                showCardOptionsSheet = false
                onEditRequest(selectedCard?.id)
            },
            onDeleteRequest = {
                showCardOptionsSheet = false
                showDeleteCardDialog = true
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
                            message = context.getString(R.string.message_card_deleted),
                            type = ToastType.Success
                        )
                        viewModel.loadCards(isRefreshing = true)
                    } else {
                        toaster.show(
                            message = context.getString(R.string.message_went_wrong),
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
    AnimatedContent(targetState = loadingState) { state ->
        when (state) {
            LoadingState.LOADING -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LoadingState.EMPTY -> {
                ErrorView(
                    icon = painterResource(R.drawable.vector_credit_card),
                    title = stringResource(R.string.message_no_card),
                    description = stringResource(R.string.message_no_card_description)
                )
            }

            LoadingState.LOADED -> {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.loadCards(isRefreshing = true)
                        }
                    }
                ) {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                        }
                        items(
                            items = cards,
                            key = { it.id }
                        ) { item ->
                            ReorderableItem(
                                state = reorderableLazyListState,
                                key = item.id
                            ) { isDragging ->
                                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                                CardItem(
                                    card = item,
                                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                                    isCvv2VisibleByDefault = !isSecretCvv2InList,
                                    cardElevation = elevation,
                                    dragHandleModifier = Modifier.draggableHandle(
                                        onDragStarted = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                        },
                                        onDragStopped = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        }
                                    ),
                                    onClick = {
                                        selectedCard = item
                                        showCardOptionsSheet = true
                                    }
                                )
                            }
                            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
                        }
                        item {
                            Spacer(Modifier.height(80.dp))
                        }
                    }
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
    val viewModel = MainViewModel.getInstance(db)
    HomeScreen(
        viewModel = viewModel,
        toaster = rememberToasterState(),
        onEditRequest = {}
    )
}
