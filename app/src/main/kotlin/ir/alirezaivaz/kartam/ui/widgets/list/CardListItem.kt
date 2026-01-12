package ir.alirezaivaz.kartam.ui.widgets.list

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.utils.BiometricHelper

@Composable
fun CardListItem(
    card: CardInfo,
    authType: AuthType,
    isDragging: Boolean,
    isCvv2VisibleByDefault: Boolean,
    isAuthOwnedCardDetails: Boolean,
    isAuthenticationRequired: Boolean,
    toaster: ToasterState,
    onCardSelect: (card: CardInfo) -> Unit,
    modifier: Modifier = Modifier,
    dragHandleModifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
    CardItem(
        card = card,
        modifier = modifier,
        isCvv2VisibleByDefault = isCvv2VisibleByDefault,
        isAuthenticationRequired = isAuthenticationRequired,
        cardElevation = elevation,
        dragHandleModifier = dragHandleModifier,
        onClick = {
            if (card.isOwned && isAuthOwnedCardDetails) {
                BiometricHelper(
                    activity = activity as FragmentActivity,
                    authType = authType,
                    onResult = {
                        if (it) {
                            onCardSelect(card)
                        } else {
                            toaster.show(
                                message = resources.getString(R.string.error_authentication_failed),
                                type = ToastType.Error
                            )
                        }
                    }
                ).authenticate()
            } else {
                onCardSelect(card)
            }
        },
        onAuthenticationFailed = {
            toaster.show(
                message = resources.getString(R.string.error_authentication_failed),
                type = ToastType.Error
            )
        }
    )
}
