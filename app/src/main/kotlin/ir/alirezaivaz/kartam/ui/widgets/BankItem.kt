package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankItem(
    bank: Bank,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (bank.prefixes.isEmpty()) {
                    scope.launch {
                        tooltipState.show()
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_horizontal))
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
        Text(
            text = stringResource(bank.title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        if (bank.prefixes.isEmpty()) {
            TooltipBox(
                state = tooltipState,
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                tooltip = {
                    RichTooltip(
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.supported_bank_no_auto_detect,
                                stringResource(bank.title)
                            )
                        )
                    }
                }
            ) {
                IconButton(
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_spacing)),
                    content = {
                        Icon(
                            painter = painterResource(TablerIcons.ProgressCheck),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        scope.launch {
                            tooltipState.show()
                        }
                    }
                )
            }
        }
    }
}
