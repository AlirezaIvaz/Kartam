package ir.alirezaivaz.kartam.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@Composable
fun FilterChip(
    @DrawableRes
    icon: Int?,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier.handPointerIcon(),
        selected = isSelected,
        label = {
            Text(text = label)
        },
        leadingIcon = if (isSelected && icon != null) {
            {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        onClick = onClick,
    )
}
