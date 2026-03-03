package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.CardInfo
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.ShareUiState
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.ui.widgets.ShareItemCheckBox
import ir.alirezaivaz.kartam.ui.widgets.SwitchItem
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.alirezaivaz.kartam.utils.Utils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareCardSheet(
    card: CardInfo?,
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
            ShareCardSheetContent(
                card = card,
                toaster = toaster
            )
        }
    }
}

@Composable
fun ShareCardSheetContent(
    card: CardInfo,
    toaster: ToasterState,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val resources = LocalResources.current
    val clipboard = LocalClipboard.current
    val language by SettingsManager.language.collectAsState()
    val isForeign = language == Language.English && !card.englishName.isNullOrEmpty()
    val ownerName = card.englishName?.takeIf { isForeign && it.isNotBlank() } ?: card.name
    var state by remember {
        mutableStateOf(ShareUiState())
    }
    val previewText = remember(state) {
        Utils.buildShareText(
            card = card,
            state = state,
            isForeign = isForeign,
            resources = resources
        )
    }
    val isAnythingSelected =
        state.includeCardNumber ||
                state.includeOwnerName ||
                state.includeBankName ||
                state.includeAccountNumber ||
                state.includeShabaNumber

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.extraLarge),
    ) {
        Text(
            text = stringResource(R.string.action_share),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.large)
        )
        VerticalSpacer(height = Dimens.medium)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.large)
            ) {
                Text(
                    text = if (isAnythingSelected) {
                        previewText
                    } else {
                        stringResource(R.string.message_nothing_to_share)
                    },
                    modifier = Modifier
                        .padding(Dimens.large)
                        .animateContentSize(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            VerticalSpacer(height = Dimens.medium)
            ShareItemCheckBox(
                title = stringResource(R.string.label_card_number),
                content = card.number,
                isChecked = state.includeCardNumber,
                onCheckedChange = {
                    state = state.copy(includeCardNumber = it)
                },
            )
            card.shabaNumber?.let { shabaNumber ->
                ShareItemCheckBox(
                    title = stringResource(R.string.label_shaba_number),
                    content = stringResource(R.string.formatter_shaba_number).format(shabaNumber),
                    isChecked = state.includeShabaNumber,
                    onCheckedChange = {
                        state = state.copy(includeShabaNumber = it)
                    }
                )
            }
            card.accountNumber?.let { accountNumber ->
                ShareItemCheckBox(
                    title = stringResource(R.string.label_account_number),
                    content = accountNumber,
                    isChecked = state.includeAccountNumber,
                    onCheckedChange = {
                        state = state.copy(includeAccountNumber = it)
                    }
                )
            }
            ShareItemCheckBox(
                title = stringResource(R.string.label_card_owner),
                content = ownerName,
                isChecked = state.includeOwnerName,
                onCheckedChange = {
                    state = state.copy(includeOwnerName = it)
                }
            )
            ShareItemCheckBox(
                title = stringResource(R.string.label_bank),
                content = stringResource(card.bank.title),
                isChecked = state.includeBankName,
                onCheckedChange = {
                    state = state.copy(includeBankName = it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.label_advanced_format),
                isChecked = state.advancedFormat,
                onCheckedChanged = {
                    state = state.copy(advancedFormat = it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.label_persian_digits),
                isChecked = state.usePersianDigits,
                onCheckedChanged = {
                    state = state.copy(usePersianDigits = it)
                }
            )
            SwitchItem(
                title = stringResource(R.string.label_card_number_separator),
                isChecked = state.useCardSeparator,
                onCheckedChanged = {
                    state = state.copy(useCardSeparator = it)
                }
            )
            VerticalSpacer(height = Dimens.small)
        }
        VerticalSpacer(height = Dimens.small)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.small)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                enabled = isAnythingSelected,
                onClick = {
                    scope.launch {
                        Utils.copyTextToClipboard(
                            clipboard = clipboard,
                            label = resources.getString(R.string.app_name),
                            text = previewText,
                            onSuccess = {
                                toaster.show(
                                    message = resources.getString(R.string.message_copied_to_clipboard),
                                    type = ToastType.Success
                                )
                            },
                            onFailure = {
                                toaster.show(
                                    message = resources.getString(R.string.error_unknown),
                                    type = ToastType.Error
                                )
                            }
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.action_copy)
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                enabled = isAnythingSelected,
                onClick = {
                    Utils.shareText(
                        text = previewText,
                        context = context,
                        resources = resources,
                        onSuccess = {},
                        onFailure = {
                            toaster.show(
                                message = resources.getString(R.string.error_unknown),
                                type = ToastType.Error
                            )
                        }
                    )
                }
            ) {
                Text(
                    text = stringResource(R.string.action_share)
                )
            }
        }
    }
}
