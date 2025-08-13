package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.parseChangelog
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.ChangelogLine

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
            horizontal = dimensionResource(R.dimen.padding_horizontal)
        )
    ) {
        item {
            Text(
                text = stringResource(R.string.changelog),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        }
        items(changelog) { version ->
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
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
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text(
                    text = stringResource(R.string.action_i_got_it)
                )
            }
            Spacer(Modifier.height(80.dp))
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
