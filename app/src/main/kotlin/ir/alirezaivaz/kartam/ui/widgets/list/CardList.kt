package ir.alirezaivaz.kartam.ui.widgets.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing)),
        contentPadding = PaddingValues(
            start = dimensionResource(R.dimen.padding_horizontal),
            end = dimensionResource(R.dimen.padding_horizontal),
            top = dimensionResource(R.dimen.padding_vertical),
            bottom = 80.dp
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
