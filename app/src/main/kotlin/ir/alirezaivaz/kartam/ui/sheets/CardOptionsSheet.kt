package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardItem
import ir.alirezaivaz.kartam.dto.FakeData
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.CardOptionItem
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.ui.widgets.SnapshotableCard
import ir.alirezaivaz.kartam.utils.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardOptionsSheet(
    card: CardItem?,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onSnapshotReady: (ImageBitmap) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()
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
                onEditRequest = onEditRequest,
                onDeleteRequest = onDeleteRequest,
                onSnapshotReady = onSnapshotReady,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun CardOptionsSheetContent(
    card: CardItem,
    toaster: ToasterState,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onSnapshotReady: (ImageBitmap) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val isSecretCvv2InDetails by SettingsManager.isSecretCvv2InDetails.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.padding_vertical)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(card.bank.logo),
            contentDescription = stringResource(card.bank.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier.heightIn(min = 50.dp)
        )
    }
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        SnapshotableCard(
            onSnapshotReady = onSnapshotReady
        ) {
            CardItem(
                card = card,
                isCvv2VisibleByDefault = !isSecretCvv2InDetails
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        HorizontalDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        if (card.number.isNotEmpty()) {
            CardOptionItem(
                title = stringResource(R.string.label_card_number),
                subtitle = card.number,
                toaster = toaster
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        }
        if (!card.shabaNumber.isNullOrEmpty()) {
            CardOptionItem(
                title = stringResource(R.string.label_shaba_number),
                subtitle = stringResource(R.string.formatter_shaba_number).format(card.shabaNumber),
                toaster = toaster
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        }
        if (!card.accountNumber.isNullOrEmpty()) {
            CardOptionItem(
                title = stringResource(R.string.label_account_number),
                subtitle = card.accountNumber,
                toaster = toaster
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        }
        if (card.branchCode != null && card.branchCode > 0) {
            CardOptionItem(
                title = stringResource(R.string.label_branch_code),
                subtitle = card.branchCode.toString(),
                toaster = toaster
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        }
        if (!card.branchName.isNullOrEmpty()) {
            CardOptionItem(
                title = stringResource(R.string.label_branch_name),
                subtitle = card.branchName,
                subtitleFont = MaterialTheme.typography.bodyLarge.fontFamily,
                toaster = toaster
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_spacing)))
        HorizontalDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_horizontal))
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier.weight(1f),
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
                modifier = Modifier.weight(1f),
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
        Spacer(Modifier.height(80.dp))
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
                onEditRequest = {},
                onDeleteRequest = {},
                onSnapshotReady = {},
                onDismissRequest = {}
            )
        }
    }
}
