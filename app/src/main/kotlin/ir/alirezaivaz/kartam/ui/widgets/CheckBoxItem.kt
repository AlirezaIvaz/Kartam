package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.Dimens

@Composable
fun CheckBoxItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    content: (@Composable (modifier: Modifier) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) {
                onCheckedChange(!isChecked)
            }
            .padding(
                vertical = Dimens.extraSmall,
                horizontal = Dimens.large,
            )
            .handPointerIcon(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (content != null) {
            content(
                Modifier
                    .weight(1f)
                    .alpha(if (isEnabled) 1f else 0.38f)
            )
        } else {
            Text(
                title,
                style = titleStyle,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = if (isEnabled) 1f else 0.38f
                ),
                modifier = Modifier.weight(1f)
            )
        }
        Checkbox(
            checked = isChecked,
            enabled = isEnabled,
            onCheckedChange = {
                onCheckedChange(!isChecked)
            }
        )
    }
}
