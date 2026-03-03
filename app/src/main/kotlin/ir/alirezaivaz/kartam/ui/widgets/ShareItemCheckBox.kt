package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShareItemCheckBox(
    title: String,
    content: String,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    CheckBoxItem(
        title = title,
        isChecked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        isEnabled = isEnabled
    ) { modifier ->
        Column(
            modifier = modifier
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = content,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
