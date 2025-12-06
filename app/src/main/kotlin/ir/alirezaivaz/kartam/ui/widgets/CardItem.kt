package ir.alirezaivaz.kartam.ui.widgets

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.FakeData
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.toStringOrNull
import ir.alirezaivaz.kartam.extensions.formattedExpirationDate
import ir.alirezaivaz.kartam.extensions.formattedShabaNumber
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.theme.aradFontFamily
import ir.alirezaivaz.kartam.ui.theme.kodeMonoFontFamily
import ir.alirezaivaz.kartam.ui.theme.montserratFontFamily
import ir.alirezaivaz.kartam.utils.BiometricHelper
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.alirezaivaz.kartam.utils.Utils

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardItem(
    card: CardInfo,
    modifier: Modifier = Modifier,
    dragHandleModifier: Modifier = Modifier,
    cardElevation: Dp = 0.dp,
    isCvv2VisibleByDefault: Boolean = false,
    isAuthenticationRequired: Boolean = false,
    onClick: (() -> Unit)? = null,
    onAuthenticationFailed: (() -> Unit)? = null
) {
    val activity = LocalActivity.current
    val language by SettingsManager.language.collectAsState()
    var isCvv2Visible by remember { mutableStateOf(isCvv2VisibleByDefault) }
    val foreignByDefault = language == Language.English && !card.englishName.isNullOrEmpty()
    var isForeignNameVisible by remember { mutableStateOf(foreignByDefault) }
    val authType by SettingsManager.authType.collectAsState()
    val isShowShabaNumberInCard by SettingsManager.isShowShabaNumberInCard.collectAsState()
    val isShowFullExpirationDate by SettingsManager.isShowFullExpirationDate.collectAsState()
    val isShowReverseExpirationDate by SettingsManager.isShowReverseExpirationDate.collectAsState()
    DirectionLayout(LayoutDirection.Ltr) {
        Card(
            modifier = modifier
                .handPointerIcon(enabled = onClick != null)
                .shadow(
                    elevation = cardElevation,
                    shape = CardDefaults.shape,
                )
                .clip(CardDefaults.shape)
                .clickable(enabled = onClick != null) {
                    onClick?.invoke()
                }
        ) {
//        Box {
//            Image(
//                painter = painterResource(card.bank.logo),
//                contentDescription = stringResource(card.bank.title),
//                modifier = Modifier
//                    .alpha(0.2f)
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 22.dp)
//                    .height(28.dp)
//            )
//        }
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_horizontal),
                    vertical = dimensionResource(R.dimen.padding_vertical)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                ) {
                    AnimatedContent(
                        targetState = card.bank,
                        transitionSpec = {
                            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                        },
                        label = "BankIconAnimation"
                    ) { bank ->
                        Image(
                            painter = painterResource(bank.icon),
                            modifier = Modifier
                                .height(48.dp)
                                .widthIn(max = 48.dp),
                            contentDescription = stringResource(bank.title)
                        )
                    }
                    AnimatedContent(
                        targetState = isForeignNameVisible,
                        modifier = Modifier
                            .weight(1f)
                    ) { isForeign ->
                        val fontFamily = if (isForeign && !card.englishName.isNullOrBlank()) {
                            montserratFontFamily
                        } else {
                            aradFontFamily
                        }
                        Text(
                            text = card.englishName?.takeIf { isForeign && it.isNotBlank() } ?: card.name,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .then(
                                    if (card.englishName.isNullOrBlank()) {
                                        Modifier
                                    } else {
                                        Modifier.clickable {
                                            isForeignNameVisible = !isForeignNameVisible
                                        }
                                    }
                                ),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.W500,
                            fontFamily = fontFamily
                        )
                    }
                    AnimatedContent(
                        targetState = card.bank.isNeo,
                        transitionSpec = {
                            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                        },
                        label = "ShetabAnimation"
                    ) { isNeo ->
                        Image(
                            painter = painterResource(
                                if (isNeo) {
                                    R.drawable.ic_shetab_neo
                                } else {
                                    R.drawable.ic_shetab
                                }
                            ),
                            modifier = dragHandleModifier
                                .height(24.dp)
                                .widthIn(min = 32.dp),
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                AnimatedContent(
                    Utils.getCardNumber(card.number),
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },
                    label = "CardNumberChangeAnimation"
                ) { number ->
                    Text(
                        text = number,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = kodeMonoFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                AnimatedVisibility(isShowShabaNumberInCard && !card.shabaNumber.isNullOrEmpty()) {
                    Text(
                        text = card.shabaNumber?.formattedShabaNumber() ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.padding_spacing))
                            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                        textAlign = TextAlign.Start,
                        fontFamily = kodeMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
                AnimatedVisibility(
                    visible = (!card.expirationMonth?.toString().isNullOrBlank() && !card.expirationYear?.toString().isNullOrBlank())
                            || !card.cvv2?.toStringOrNull().isNullOrBlank(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.padding_spacing))
                            .padding(horizontal = dimensionResource(R.dimen.padding_spacing)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                    ) {
                        AnimatedVisibility(
                            visible = !card.expirationMonth?.toString().isNullOrBlank() && !card.expirationYear?.toString().isNullOrBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                            ) {
                                Text(
                                    text = stringResource(R.string.label_exp),
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_spacing)),
                                    fontWeight = FontWeight.W300
                                )
                                Text(
                                    text = stringResource(R.string.formatter_exp_date).formattedExpirationDate(
                                        showFull = isShowFullExpirationDate,
                                        showReverse = isShowReverseExpirationDate,
                                        expirationMonth = card.expirationMonth ?: 0,
                                        expirationYear = card.expirationYear ?: 0
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_spacing)),
                                    fontFamily = kodeMonoFontFamily,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        AnimatedVisibility(
                            visible = !card.cvv2?.toStringOrNull().isNullOrBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row(
                                modifier = Modifier
                                    .handPointerIcon()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        if (authType != AuthType.None && isAuthenticationRequired) {
                                            if (isCvv2Visible) {
                                                isCvv2Visible = false
                                            } else {
                                                BiometricHelper(
                                                    activity = activity as FragmentActivity,
                                                    authType = authType,
                                                    onResult = {
                                                        if (it) {
                                                            isCvv2Visible = true
                                                        } else {
                                                            onAuthenticationFailed?.invoke()
                                                        }
                                                    }
                                                ).authenticate()
                                            }
                                        } else {
                                            isCvv2Visible = !isCvv2Visible
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                            ) {
                                Text(
                                    text = stringResource(R.string.label_cvv2),
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_spacing)),
                                    fontWeight = FontWeight.W300
                                )
                                AnimatedContent(
                                    targetState = isCvv2Visible,
                                    transitionSpec = {
                                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                                    },
                                    label = "ObfuscationAnimation"
                                ) { visible ->
                                    AnimatedContent(
                                        Utils.getCvv2(card.cvv2?.toStringOrNull(), visible),
                                        transitionSpec = {
                                            fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                                        },
                                        label = "Cvv2ChangeAnimation"
                                    ) { cvv2 ->
                                        Text(
                                            text = cvv2,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_spacing)),
                                            fontFamily = kodeMonoFontFamily,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            }
        }
    }
}

@Preview
@Composable
fun CardItemPreview() {

    KartamTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            FakeData.cards.forEach {
                CardItem(card = it)
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
            }
        }
    }
}
