package ir.alirezaivaz.kartam.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dokar.sonner.ToastType
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.AddCardActivity
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.LoadingState
import ir.alirezaivaz.kartam.ui.sheets.CardOptionsSheet
import ir.alirezaivaz.kartam.ui.dialogs.DeleteCardDialog
import ir.alirezaivaz.kartam.ui.sheets.ChangelogSheet
import ir.alirezaivaz.kartam.ui.sheets.SettingsSheet
import ir.alirezaivaz.kartam.ui.sheets.SupportedBanksSheet
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.viewmodel.MainViewModel
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.ErrorView
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.utils.AppDatabase
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val toaster = rememberToasterState()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val hapticFeedback = LocalHapticFeedback.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val db = AppDatabase.getInstance(context)
    val viewModel = MainViewModel.getInstance(db)
    val loadingState by viewModel.loadingState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val cards by viewModel.cards.collectAsState()
    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }
    var selectedCardSnapshot by remember { mutableStateOf<ImageBitmap?>(null) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showChangelogSheet by remember { mutableStateOf(SettingsManager.isAppUpdated()) }
    var showCardOptionsSheet by remember { mutableStateOf(false) }
    var showSupportedBanksSheet by remember { mutableStateOf(false) }
    var showDeleteCardDialog by remember { mutableStateOf(false) }
    val isSecretCvv2InList by SettingsManager.isSecretCvv2InList.collectAsState()
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.onMove(from.index, to.index)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED && loadingState != LoadingState.LOADING) {
            viewModel.loadCards(isRefreshing = loadingState == LoadingState.LOADED)
        }
    }

    KartamTheme {
        Surface {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        actions = {
                            IconButton(
                                enabled = loadingState != LoadingState.LOADING,
                                onClick = {
                                    showOptionsMenu = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(TablerIcons.DotsVertical),
                                    contentDescription = stringResource(R.string.action_more_options)
                                )
                            }
                            DropdownMenu(
                                expanded = showOptionsMenu,
                                offset = DpOffset(
                                    x = (-8).dp,
                                    y = 0.dp
                                ),
                                onDismissRequest = {
                                    showOptionsMenu = false
                                }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.action_reload)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.Reload),
                                            contentDescription = stringResource(R.string.action_reload)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        scope.launch(Dispatchers.IO) {
                                            viewModel.loadCards(isRefreshing = loadingState == LoadingState.LOADED)
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.action_rate)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.Message),
                                            contentDescription = stringResource(R.string.action_rate)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        try {
                                            if (BuildConfig.FLAVOR == "telegram") {
                                                uriHandler.openUri(BuildConfig.RATE_URL)
                                            } else {
                                                val intentAction = if (BuildConfig.FLAVOR == "cafebazaar") {
                                                    Intent.ACTION_EDIT
                                                } else {
                                                    Intent.ACTION_VIEW
                                                }
                                                context.startActivity(
                                                    Intent(
                                                        intentAction,
                                                        BuildConfig.RATE_URL.toUri()
                                                    )
                                                )
                                            }
                                        } catch (_: Exception) {
                                            toaster.show(
                                                message = context.getString(R.string.error_request_run_failed),
                                                type = ToastType.Error
                                            )
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.action_more_apps)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.Apps),
                                            contentDescription = stringResource(R.string.action_more_apps)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        try {
                                            if (BuildConfig.FLAVOR == "telegram") {
                                                uriHandler.openUri(BuildConfig.APPS_URL)
                                            } else {
                                                val intentAction = Intent.ACTION_VIEW
                                                context.startActivity(
                                                    Intent(
                                                        intentAction,
                                                        BuildConfig.APPS_URL.toUri()
                                                    )
                                                )
                                            }
                                        } catch (_: Exception) {
                                            toaster.show(
                                                message = context.getString(R.string.error_request_run_failed),
                                                type = ToastType.Error
                                            )
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.changelog)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.List),
                                            contentDescription = stringResource(R.string.changelog)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        showChangelogSheet = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.supported_banks)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.BuildingBank),
                                            contentDescription = stringResource(R.string.supported_banks)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        showSupportedBanksSheet = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.action_settings)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(TablerIcons.Settings),
                                            contentDescription = stringResource(R.string.action_settings)
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = false
                                        showSettingsSheet = true
                                    }
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = loadingState != LoadingState.LOADING,
                    ) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(R.string.action_add_card)
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.action_add_card)
                                )
                            },
                            onClick = {
                                val intent = Intent(context, AddCardActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                KartamToaster(state = toaster)
                if (showChangelogSheet) {
                    ChangelogSheet(
                        onDismissRequest = {
                            SettingsManager.setLastVersion()
                            showChangelogSheet = false
                        }
                    )
                }
                if (showSettingsSheet) {
                    SettingsSheet(
                        onDismissRequest = {
                            showSettingsSheet = false
                        },
                        onRefreshRequest = {
                            scope.launch {
                                viewModel.loadCards()
                            }
                        },
                        onThemeChangedRequest = { item ->
                            scope.launch {
                                delay(500)
                                SettingsManager.setTheme(item)
                            }
                        },
                        onLanguageChangedRequest = { item ->
                            scope.launch {
                                delay(500)
                                SettingsManager.setLanguage(item)
                            }
                        }
                    )
                }
                if (showCardOptionsSheet) {
                    CardOptionsSheet(
                        card = selectedCard,
                        onEditRequest = {
                            showCardOptionsSheet = false
                            val intent = Intent(context, AddCardActivity::class.java)
                            intent.putExtra("id", selectedCard?.id)
                            context.startActivity(intent)
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
                if (showSupportedBanksSheet) {
                    SupportedBanksSheet(
                        onDismissRequest = {
                            showSupportedBanksSheet = false
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
                                        message = "Card deleted successfully!",
                                        type = ToastType.Success
                                    )
                                    viewModel.loadCards(isRefreshing = true)
                                } else {
                                    toaster.show(
                                        message = "Action not permitted!"
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
                    targetState = loadingState,
                    modifier = Modifier.padding(innerPadding)
                ) { state ->
                    when(state) {
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
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
