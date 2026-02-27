package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@Composable
fun SwitchItem(
    title: String,
    isChecked: Boolean,
    onCheckedChanged: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    paddingStart: Dp = dimensionResource(R.dimen.padding_horizontal),
    paddingEnd: Dp = dimensionResource(R.dimen.padding_horizontal),
) {
    Row(
        modifier = modifier
            .handPointerIcon()
            .fillMaxWidth()
            .clickable(enabled = isEnabled) {
                onCheckedChanged(!isChecked)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = if (isEnabled) 1f else 0.38f
            ),
            modifier = Modifier.padding(start = paddingStart)
        )
        Switch(
            checked = isChecked,
            enabled = isEnabled,
            modifier = Modifier.padding(end = paddingEnd),
            onCheckedChange = {
                onCheckedChanged(!isChecked)
            }
        )
    }
}
