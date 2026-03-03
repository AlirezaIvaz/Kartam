package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.FakeData
import ir.alirezaivaz.kartam.dto.toStringOrNull
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.CardOptionItem
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.ui.widgets.SnapshotableCard
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer
import ir.alirezaivaz.kartam.utils.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardOptionsSheet(
    card: CardInfo?,
    onShareRequest: () -> Unit,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onSnapshotReady: (ImageBitmap) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val toaster = rememberToasterState()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        KartamToaster(state = toaster)
        if (card != null) {
            CardOptionsSheetContent(
                card = card,
                toaster = toaster,
                onShareRequest = onShareRequest,
                onEditRequest = onEditRequest,
                onDeleteRequest = onDeleteRequest,
                onSnapshotReady = onSnapshotReady,
            )
        }
    }
}

@Composable
fun CardOptionsSheetContent(
    card: CardInfo,
    toaster: ToasterState,
    onShareRequest: () -> Unit,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onSnapshotReady: (ImageBitmap) -> Unit,
) {
    val resources = LocalResources.current
    val isAuthSecretData by SettingsManager.isAuthSecretData.collectAsState()
    val isAuthOwnedCardDetails by SettingsManager.isAuthOwnedCardDetails.collectAsState()
    val isSecretCvv2InDetails by SettingsManager.isSecretCvv2InDetails.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimens.large,
                end = Dimens.large,
                bottom = Dimens.extraLarge
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(card.bank.logo),
                contentDescription = stringResource(card.bank.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.heightIn(min = 50.dp)
            )
            IconButton(
                onClick = onShareRequest,
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.filledTonalIconButtonColors()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = stringResource(R.string.action_share)
                )
            }
        }
        VerticalSpacer(height = Dimens.large)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(height = Dimens.large)
            SnapshotableCard(
                onSnapshotReady = onSnapshotReady
            ) {
                CardItem(
                    card = card,
                    isCvv2VisibleByDefault = !isSecretCvv2InDetails,
                    isAuthenticationRequired = !isAuthOwnedCardDetails && isAuthSecretData,
                    onAuthenticationFailed = {
                        toaster.show(
                            message = resources.getString(R.string.error_authentication_failed),
                            type = ToastType.Error
                        )
                    }
                )
            }
            VerticalSpacer(height = Dimens.large)
            HorizontalDivider()
            VerticalSpacer(height = Dimens.large)
            if (card.number.isNotEmpty()) {
                CardOptionItem(
                    title = stringResource(R.string.label_card_number),
                    subtitle = card.number,
                )
                VerticalSpacer(height = Dimens.small)
            }
            if (!card.shabaNumber.isNullOrEmpty()) {
                CardOptionItem(
                    title = stringResource(R.string.label_shaba_number),
                    subtitle = stringResource(R.string.formatter_shaba_number).format(card.shabaNumber),
                )
                VerticalSpacer(height = Dimens.small)
            }
            if (!card.accountNumber.isNullOrEmpty()) {
                CardOptionItem(
                    title = stringResource(R.string.label_account_number),
                    subtitle = card.accountNumber,
                )
                VerticalSpacer(height = Dimens.small)
            }
            card.firstCode.toStringOrNull()?.let {
                CardOptionItem(
                    title = stringResource(R.string.label_first_code),
                    subtitle = it,
                )
                VerticalSpacer(height = Dimens.small)
            }
            if (card.branchCode != null && card.branchCode > 0) {
                CardOptionItem(
                    title = stringResource(R.string.label_branch_code),
                    subtitle = card.branchCode.toString(),
                )
                VerticalSpacer(height = Dimens.small)
            }
            if (!card.branchName.isNullOrEmpty()) {
                CardOptionItem(
                    title = stringResource(R.string.label_branch_name),
                    subtitle = card.branchName,
                    subtitleFont = MaterialTheme.typography.bodyLarge.fontFamily,
                )
                VerticalSpacer(height = Dimens.small)
            }
            card.accountType?.let {
                CardOptionItem(
                    title = stringResource(R.string.account_type),
                    subtitle = stringResource(card.accountType.title),
                    subtitleFont = MaterialTheme.typography.bodyLarge.fontFamily,
                )
                VerticalSpacer(height = Dimens.small)
            }
            if (!card.comment.isNullOrEmpty()) {
                CardOptionItem(
                    title = stringResource(R.string.label_comment),
                    subtitle = card.comment,
                    subtitleFont = MaterialTheme.typography.bodyLarge.fontFamily,
                    subtitleMaxLines = Int.MAX_VALUE,
                )
                VerticalSpacer(height = Dimens.small)
            }
            VerticalSpacer(height = Dimens.small)
        }
        HorizontalDivider()
        VerticalSpacer(height = Dimens.small)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.large)
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .handPointerIcon()
                    .weight(1f),
                contentColor = MaterialTheme.colorScheme.tertiary,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                text = {
                    Text(
                        text = stringResource(R.string.action_edit_card)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.action_edit_card)
                    )
                },
                onClick = {
                    onEditRequest()
                }
            )
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .handPointerIcon()
                    .weight(1f),
                contentColor = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                text = {
                    Text(
                        text = stringResource(R.string.action_delete_card)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = stringResource(R.string.action_delete_card)
                    )
                },
                onClick = {
                    onDeleteRequest()
                }
            )
        }
    }
}

@Preview
@Composable
fun CardOptionsSheetPreview() {
    KartamTheme {
        Surface {
            CardOptionsSheetContent(
                card = FakeData.bluCard,
                toaster = rememberToasterState(),
                onShareRequest = {},
                onEditRequest = {},
                onDeleteRequest = {},
                onSnapshotReady = {},
            )
        }
    }
}
