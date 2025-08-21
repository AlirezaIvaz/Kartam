package ir.alirezaivaz.kartam.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.BankType
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.BankItem

@Composable
fun SupportedBanksScreen() {
    val context = LocalContext.current
    val baseBanks = Bank.entries.filter { it != Bank.Unknown }
    val normalBanks =
        baseBanks.filter { it.type == BankType.Bank }.sortedBy { context.getString(it.title) }
    val neoBanks =
        baseBanks.filter { it.type == BankType.NeoBank }.sortedBy { context.getString(it.title) }
    val creditInstitutions = baseBanks.filter { it.type == BankType.CreditInstitution }
        .sortedBy { context.getString(it.title) }
    LazyColumn {
        item {
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Text(
                text = stringResource(R.string.supported_banks_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            )
        }
        item {
            Text(
                text = stringResource(R.string.supported_banks_normal),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    .padding(
                        top = dimensionResource(R.dimen.padding_vertical),
                        bottom = dimensionResource(R.dimen.padding_spacing)
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
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    .padding(
                        top = dimensionResource(R.dimen.padding_vertical),
                        bottom = dimensionResource(R.dimen.padding_spacing)
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
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                    .padding(
                        top = dimensionResource(R.dimen.padding_vertical),
                        bottom = dimensionResource(R.dimen.padding_spacing)
                    )
            )
        }
        items(creditInstitutions) { bank ->
            BankItem(bank = bank)
        }
        item {
            Spacer(Modifier.height(80.dp))
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
