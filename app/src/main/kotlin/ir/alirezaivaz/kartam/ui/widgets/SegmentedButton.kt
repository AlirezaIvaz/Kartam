package ir.alirezaivaz.kartam.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@Composable
fun SingleChoiceSegmentedButtonRowScope.SegmentedButton(
    index: Int,
    count: Int,
    @StringRes
    label: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    @DrawableRes
    iconSelected: Int? = null,
) {
    SegmentedButton(
        modifier = modifier.handPointerIcon(),
        selected = isSelected,
        icon = {
            if (icon != null) {
                Icon(
                    painter = painterResource(
                        if (isSelected && iconSelected != null) {
                            iconSelected
                        } else {
                            icon
                        }
                    ),
                    contentDescription = stringResource(label)
                )
            } else {
                SegmentedButtonDefaults.Icon(isSelected)
            }
        },
        shape = SegmentedButtonDefaults.itemShape(
            index = index,
            count = count
        ),
        onClick = onClick,
        label = {
            Text(
                text = stringResource(label)
            )
        }
    )
}
