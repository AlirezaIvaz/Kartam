package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.DetectedCardData
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.widgets.CardItem
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardNumberDetectedSheet(
    detectedCardData: DetectedCardData,
    onAddCardRequest: (data: DetectedCardData) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        CardNumberDetectedSheetContent(
            data = detectedCardData,
            onAddCardRequest = onAddCardRequest,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun CardNumberDetectedSheetContent(
    data: DetectedCardData,
    onAddCardRequest: (data: DetectedCardData) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val cardInfo = CardInfo(
        name = data.name.orEmpty(),
        number = data.cardNumber.orEmpty(),
        shabaNumber = data.shabaNumber,
        accountNumber = null,
        expirationMonth = null,
        expirationYear = null,
        cvv2 = null,
        bank = Bank.fromCardNumber(data.cardNumber.orEmpty()),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.extraLarge),
    ) {
        Text(
            text = stringResource(R.string.action_add_copied_card),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.large)
        )
        VerticalSpacer(height = Dimens.medium)
        CardItem(
            card = cardInfo,
            modifier = Modifier.padding(horizontal = Dimens.large)
        )
        VerticalSpacer(height = Dimens.extraLarge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.small)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.action_dismiss)
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onDismissRequest()
                    onAddCardRequest(data)
                }
            ) {
                Text(
                    text = stringResource(R.string.action_add_card)
                )
            }
        }
    }
}
