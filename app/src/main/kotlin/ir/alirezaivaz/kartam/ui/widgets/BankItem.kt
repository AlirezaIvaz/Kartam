package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.BankType
import ir.alirezaivaz.kartam.dto.parentBank
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankItem(
    bank: Bank,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)
    TooltipBox(
        state = tooltipState,
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        enableUserInput = bank.prefixes.isEmpty(),
        tooltip = {
            RichTooltip(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                ) {
                    Image(
                        painter = painterResource(
                            if (bank.isMerged && bank.parentBank != null) {
                                bank.parentBank!!.icon
                            } else {
                                bank.icon
                            }
                        ),
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.padding_spacing))
                            .padding(vertical = dimensionResource(R.dimen.padding_spacing))
                            .size(24.dp),
                        contentDescription = stringResource(bank.title)
                    )
                    Text(
                        text = if (bank.isMerged && bank.parentBank != null) {
                            stringResource(
                                R.string.supported_bank_merged,
                                stringResource(bank.title),
                                stringResource(bank.parentBank!!.title)
                            )
                        } else {
                            stringResource(
                                R.string.supported_bank_no_auto_detect,
                                stringResource(bank.title)
                            )
                        }
                    )
                }
            }
        }
    ) {
        Row(
            modifier = modifier
                .handPointerIcon()
                .fillMaxWidth()
                .clickable {
                    if (bank.prefixes.isEmpty()) {
                        scope.launch {
                            tooltipState.show()
                        }
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(bank.icon),
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_horizontal))
                    .padding(vertical = dimensionResource(R.dimen.padding_spacing))
                    .height(48.dp)
                    .width(48.dp),
                contentDescription = stringResource(bank.title)
            )
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_horizontal)))
            Text(
                text = stringResource(bank.title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_horizontal)))
            if (bank.prefixes.isEmpty()) {
                Icon(
                    painter = painterResource(
                        if (bank.isMerged) {
                            R.drawable.ic_progress_alert
                        } else {
                            R.drawable.ic_progress_check
                        }
                    ),
                    tint = if (bank.isMerged) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                    contentDescription = null
                )
            }
            if (bank.parentBank != null && bank.type == BankType.NeoBank) {
                Image(
                    painter = painterResource(bank.parentBank!!.icon),
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_spacing))
                        .padding(vertical = dimensionResource(R.dimen.padding_spacing))
                        .size(24.dp),
                    contentDescription = stringResource(bank.title)
                )
            }
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_horizontal)))
        }
    }
}
