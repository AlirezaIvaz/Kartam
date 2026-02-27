package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.extensions.parseChangelog
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.ChangelogLine
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogSheet(
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        ChangelogSheetContent(
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun ChangelogSheetContent(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val changelog = context.parseChangelog()
    LazyColumn(
        modifier = Modifier.padding(
            horizontal = Dimens.large
        )
    ) {
        item {
            Text(
                text = stringResource(R.string.changelog),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.large),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            VerticalSpacer(height = Dimens.large)
        }
        items(changelog) { version ->
            Row(
                modifier = Modifier
                    .padding(vertical = Dimens.small)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.changelog_version, version.version),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = version.date,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            version.items.forEach { item ->
                ChangelogLine(item)
            }
            VerticalSpacer(height = Dimens.medium)
        }
        item {
            FilledTonalButton(
                modifier = Modifier
                    .handPointerIcon()
                    .fillMaxWidth(),
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text(
                    text = stringResource(R.string.action_i_got_it)
                )
            }
            VerticalSpacer(height = Dimens.screenBottomPadding)
        }
    }
}

@Preview
@Composable
fun ChangelogSheetContentPreview() {
    KartamTheme {
        Surface {
            ChangelogSheetContent(
                onDismissRequest = {}
            )
        }
    }
}
