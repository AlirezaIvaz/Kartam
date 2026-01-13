package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.ui.widgets.list.CardList
import sh.calvin.reorderable.ReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KartamSearchBar(
    query: String,
    isOwned: Boolean,
    isExpanded: Boolean,
    isLoading: Boolean,
    searchResults: List<CardInfo>,
    toaster: ToasterState,
    authType: AuthType,
    isCvv2VisibleByDefault: Boolean,
    isAuthOwnedCardDetails: Boolean,
    isAuthenticationRequired: Boolean,
    reorderableLazyListState: ReorderableLazyListState,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit,
    onCardSelect: (card: CardInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier
            .fillMaxWidth()
            .padding(
                bottom = if (isExpanded) {
                    0.dp
                } else {
                    8.dp
                }
            )
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        onQueryChange(it)
                        onSearch(it)
                    },
                    onSearch = {
                        onSearch(it)
                    },
                    expanded = isExpanded,
                    onExpandedChange = {
                        onExpandChange(it)
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                if (isOwned) {
                                    R.string.action_search_in_owned
                                } else {
                                    R.string.action_search_in_others
                                }
                            ),
                        )
                    },
                    leadingIcon = {
                        if (isExpanded) {
                            IconButton(
                                onClick = {
                                    onExpandChange(false)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.action_back)
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = stringResource(R.string.action_search)
                            )
                        }
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = query.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            IconButton(
                                onClick = {
                                    onQueryChange("")
                                    onSearch("")
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_close),
                                    contentDescription = stringResource(R.string.action_clear)
                                )
                            }
                        }
                    }
                )
            },
            expanded = isExpanded,
            onExpandedChange = {
                onExpandChange(it)
            },
        ) {
            if (!isLoading && query.isNotEmpty() && searchResults.isEmpty()) {
                // TODO: Change icon
                ErrorView(
                    icon = painterResource(R.drawable.vector_credit_card),
                    title = stringResource(R.string.message_no_card),
                )
            } else if (isLoading || searchResults.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedVisibility(visible = isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CardList(
                        cards = searchResults,
                        toaster = toaster,
                        authType = authType,
                        lazyListState = lazyListState,
                        reorderableLazyListState = reorderableLazyListState,
                        isReorderable = false,
                        isCvv2VisibleByDefault = isCvv2VisibleByDefault,
                        isAuthOwnedCardDetails = isAuthOwnedCardDetails,
                        isAuthenticationRequired = isAuthenticationRequired,
                        onCardSelect = onCardSelect,
                    )
                }
            }
        }
    }
}
