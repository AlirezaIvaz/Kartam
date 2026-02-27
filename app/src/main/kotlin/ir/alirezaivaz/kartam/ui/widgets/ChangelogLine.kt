package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.alirezaivaz.kartam.dto.ChangelogItem
import ir.alirezaivaz.kartam.extensions.buildChangelogAnnotatedString
import ir.alirezaivaz.kartam.ui.theme.Dimens

@Composable
fun ChangelogLine(item: ChangelogItem, modifier: Modifier = Modifier) {
    val annotated = buildChangelogAnnotatedString(item.type, item.description)

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(bottom = Dimens.extraSmall)
    )
}
