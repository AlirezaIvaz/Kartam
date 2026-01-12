package ir.alirezaivaz.kartam.ui.widgets.list

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.CardListItemReorderable(
    card: CardInfo,
    state: ReorderableLazyListState,
    authType: AuthType,
    isCvv2VisibleByDefault: Boolean,
    isAuthOwnedCardDetails: Boolean,
    isAuthenticationRequired: Boolean,
    toaster: ToasterState,
    onCardSelect: (card: CardInfo) -> Unit,
    modifier: Modifier = Modifier,
    dragHandleModifier: Modifier = Modifier,
    isReorderable: Boolean = true,
) {
    val hapticFeedback = LocalHapticFeedback.current
    ReorderableItem(
        key = card.id,
        state = state,
        enabled = isReorderable,
    ) { isDragging ->
        CardListItem(
            card = card,
            authType = authType,
            isDragging = isDragging,
            isCvv2VisibleByDefault = isCvv2VisibleByDefault,
            isAuthOwnedCardDetails = isAuthOwnedCardDetails,
            isAuthenticationRequired = isAuthenticationRequired,
            toaster = toaster,
            onCardSelect = onCardSelect,
            modifier = modifier,
            dragHandleModifier = dragHandleModifier
                .draggableHandle(
                    enabled = isReorderable,
                    onDragStarted = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                    },
                    onDragStopped = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                    }
                ),
        )
    }
}
