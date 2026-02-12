package ir.alirezaivaz.kartam.ui.widgets.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.BackupFile
import ir.alirezaivaz.kartam.ui.widgets.DirectionLayout
import ir.mehrafzoon.composedatepicker.dto.toPrettyFormatted

@Composable
fun BackupItem(
    backup: BackupFile,
    isJalali: Boolean,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backupTime = backup.date.toPrettyFormatted(isJalali)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_file_text),
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            DirectionLayout(
                layoutDirection = LayoutDirection.Ltr
            ) {
                Text(
                    text = backup.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = backupTime,
                style = MaterialTheme.typography.labelSmall
            )
        }
        IconButton(
            onClick = onShareClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.action_share)
            )
        }
    }
}
