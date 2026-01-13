package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KartamTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: Painter? = null,
    navigationIconClick: (() -> Unit)? = null,
    navigationIconContentDescription: String? = null
) {
    CenterAlignedTopAppBar(
        actions = actions,
        modifier = modifier,
        title = {
            Text(
                text = title
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        navigationIcon = {
            navigationIcon?.let {
                IconButton(
                    modifier = Modifier.handPointerIcon(),
                    onClick = {
                        navigationIconClick?.invoke()
                    }
                ) {
                    Icon(
                        painter = navigationIcon,
                        contentDescription = navigationIconContentDescription
                    )
                }
            }
        },
    )
}
