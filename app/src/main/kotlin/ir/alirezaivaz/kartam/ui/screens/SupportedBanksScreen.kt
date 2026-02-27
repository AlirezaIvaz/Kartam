package ir.alirezaivaz.kartam.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.BankType
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.BankItem
import ir.alirezaivaz.kartam.ui.widgets.KartamTopBar

@Composable
fun SupportedBanksScreen() {
    val resources = LocalResources.current
    val baseBanks = Bank.entries.filter { it != Bank.Unknown }
    val normalBanks =
        baseBanks.filter { it.type == BankType.Bank }.sortedBy { resources.getString(it.title) }
    val neoBanks =
        baseBanks.filter { it.type == BankType.NeoBank }.sortedBy { resources.getString(it.title) }
    val creditInstitutions = baseBanks.filter { it.type == BankType.CreditInstitution }
        .sortedBy { resources.getString(it.title) }
    val totalBanks = normalBanks.size + neoBanks.size + creditInstitutions.size

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            KartamTopBar(
                title = stringResource(R.string.supported_banks),
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(
                top = Dimens.large,
                bottom = Dimens.screenBottomPadding
            )
        ) {
            item {
                Text(
                    text = stringResource(R.string.supported_banks_description, totalBanks),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.large),
                )
            }
            item {
                Text(
                    text = stringResource(R.string.supported_banks_normal),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = Dimens.large)
                        .padding(
                            top = Dimens.large,
                            bottom = Dimens.small
                        )
                )
            }
            items(normalBanks) { bank ->
                BankItem(bank = bank)
            }
            item {
                Text(
                    text = stringResource(R.string.supported_banks_neo),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = Dimens.large)
                        .padding(
                            top = Dimens.large,
                            bottom = Dimens.small
                        )
                )
            }
            items(neoBanks) { bank ->
                BankItem(bank = bank)
            }
            item {
                Text(
                    text = stringResource(R.string.supported_banks_credit_institutions),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = Dimens.large)
                        .padding(
                            top = Dimens.large,
                            bottom = Dimens.small
                        )
                )
            }
            items(creditInstitutions) { bank ->
                BankItem(bank = bank)
            }
        }
    }
}

@Preview
@Composable
fun SupportedBanksSheetContentPreview() {
    KartamTheme {
        Surface {
            SupportedBanksScreen()
        }
    }
}
