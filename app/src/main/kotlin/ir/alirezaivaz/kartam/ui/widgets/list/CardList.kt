package ir.alirezaivaz.kartam.ui.widgets.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.ui.theme.Dimens
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun CardList(
    cards: List<CardInfo>,
    toaster: ToasterState,
    authType: AuthType,
    lazyListState: LazyListState,
    reorderableLazyListState: ReorderableLazyListState,
    isCvv2VisibleByDefault: Boolean,
    isAuthOwnedCardDetails: Boolean,
    isAuthenticationRequired: Boolean,
    onCardSelect: (card: CardInfo) -> Unit,
    modifier: Modifier = Modifier,
    dragHandleModifier: Modifier = Modifier,
    isReorderable: Boolean = true,
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.small),
        contentPadding = PaddingValues(
            start = Dimens.large,
            end = Dimens.large,
            top = Dimens.large,
            bottom = Dimens.screenBottomPadding
        ),
    ) {
        items(
            items = cards,
            key = { it.id }
        ) { item ->
            CardListItemReorderable(
                card = item,
                state = reorderableLazyListState,
                toaster = toaster,
                authType = authType,
                isReorderable = isReorderable,
                isCvv2VisibleByDefault = isCvv2VisibleByDefault,
                isAuthOwnedCardDetails = isAuthOwnedCardDetails,
                isAuthenticationRequired = isAuthenticationRequired,
                dragHandleModifier = dragHandleModifier,
                onCardSelect = onCardSelect,
            )
        }
    }
}
