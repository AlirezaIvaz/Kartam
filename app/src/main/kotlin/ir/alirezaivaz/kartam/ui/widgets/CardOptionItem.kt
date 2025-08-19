package ir.alirezaivaz.kartam.ui.widgets

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.FakeData
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.theme.kodeMonoFontFamily
import kotlinx.coroutines.launch

@Composable
fun CardOptionItem(
    title: String,
    subtitle: String,
    subtitleFont: FontFamily? = kodeMonoFontFamily,
    toaster: ToasterState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.padding_vertical),
                horizontal = dimensionResource(R.dimen.padding_horizontal)
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = subtitleFont
                )
            }
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_spacing)))
            FloatingActionButton(
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText(title, subtitle)
                        val clipEntry = ClipEntry(clipData)
                        clipboard.setClipEntry(clipEntry)
                        toaster.show(
                            message = context.getString(R.string.message_copied_to_clipboard)
                                .format(title),
                            type = ToastType.Success
                        )
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = stringResource(R.string.action_copy)
                )
            }
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_spacing)))
            FloatingActionButton(
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, title)
                        putExtra(Intent.EXTRA_TEXT, subtitle)
                    }
                    val shareIntent = Intent.createChooser(
                        sendIntent,
                        context.getString(R.string.action_share_via)
                    )
                    context.startActivity(shareIntent)
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = stringResource(R.string.action_share_via)
                )
            }
        }
    }
}

@Preview
@Composable
fun CardOptionItemPreview() {
    KartamTheme {
        CardOptionItem(
            title = stringResource(R.string.label_card_number),
            subtitle = FakeData.numberBlu,
            toaster = rememberToasterState()
        )
    }
}
