package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.KartamTheme

@Composable
fun ErrorView(
    icon: Painter,
    title: String,
    description: String,
    actionButtonText: String? = null,
    actionButtonIcon: Painter? = null,
    actionButtonPressed: (() -> Unit)? = null
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenHeight = this.maxHeight
        val screenWidth = this.maxWidth
        val imageMaxHeight = screenHeight * 0.3f
        val horizontalPadding = if (screenWidth < 500.dp) 24.dp else 80.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .heightIn(max = imageMaxHeight),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                if (actionButtonText != null) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        modifier = Modifier.handPointerIcon(),
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
                            ) {
                                actionButtonIcon?.let {
                                    Icon(
                                        painter = actionButtonIcon,
                                        contentDescription = actionButtonText
                                    )
                                }
                                Text(
                                    text = actionButtonText
                                )
                            }
                        },
                        onClick = {
                            actionButtonPressed?.invoke()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    KartamTheme {
        ErrorView(
            icon = painterResource(R.drawable.vector_credit_card),
            title = stringResource(R.string.message_no_card),
            description = stringResource(R.string.message_no_card_description)
        )
    }
}
