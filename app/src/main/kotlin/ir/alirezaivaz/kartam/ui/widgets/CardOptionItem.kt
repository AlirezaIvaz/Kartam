package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.FakeData
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.theme.kodeMonoFontFamily
import ir.alirezaivaz.kartam.utils.Utils

@Composable
fun CardOptionItem(
    title: String,
    subtitle: String,
    subtitleFont: FontFamily? = kodeMonoFontFamily,
    subtitleMaxLines: Int = 1,
    isSecret: Boolean = false,
    copyAndShareAllowed: Boolean = false,
    showQuickCopyButton: Boolean = false,
    showQuickShareButton: Boolean = false,
    onCopyItemRequest: ((title: String, subtitle: String) -> Unit)? = null,
    onShareItemRequest: ((title: String, subtitle: String) -> Unit)? = null,
) {
    var isSecretVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .handPointerIcon(copyAndShareAllowed)
            .combinedClickable(
                enabled = copyAndShareAllowed,
                onClick = {
                    onCopyItemRequest?.invoke(title, subtitle)
                },
                onLongClick = {
                    onShareItemRequest?.invoke(title, subtitle)
                }
            )
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = Dimens.large,
                horizontal = Dimens.large
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.small)
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
                    text = if (isSecret && !isSecretVisible) {
                        Utils.getAsteriskString(subtitle.length.coerceIn(4, 12))
                    } else {
                        subtitle
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = subtitleMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = subtitleFont
                )
            }
            if (isSecret) {
                IconButton(
                    modifier = Modifier.handPointerIcon(),
                    onClick = { isSecretVisible = !isSecretVisible }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isSecretVisible) {
                                R.drawable.ic_eye_off
                            } else {
                                R.drawable.ic_eye
                            }
                        ),
                        contentDescription = stringResource(
                            if (isSecretVisible) {
                                R.string.action_hide_value
                            } else {
                                R.string.action_show_value
                            },
                            title
                        )
                    )
                }
            }
            if (copyAndShareAllowed && showQuickCopyButton) {
                IconButton(
                    modifier = Modifier.handPointerIcon(),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    onClick = {
                        onCopyItemRequest?.invoke(title, subtitle)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_copy),
                        contentDescription = stringResource(R.string.action_share)
                    )
                }
            }
            if (copyAndShareAllowed && showQuickShareButton) {
                IconButton(
                    modifier = Modifier.handPointerIcon(),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
                    onClick = {
                        onShareItemRequest?.invoke(title, subtitle)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = stringResource(R.string.action_share)
                    )
                }
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
        )
    }
}
