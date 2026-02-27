package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.widgets.HorizontalSpacer
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOptionsSheet(
    title: String,
    items: List<String>,
    values: List<String>,
    selectedItem: String?,
    onDismissRequest: () -> Unit,
    onItemSelectedListener: (value: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        SelectOptionsSheetContent(
            title = title,
            items = items,
            values = values,
            onDismissRequest = onDismissRequest,
            selectedItem = selectedItem,
            onItemSelectedListener = onItemSelectedListener
        )
    }
}

@Composable
fun SelectOptionsSheetContent(
    title: String,
    items: List<String>,
    values: List<String>,
    selectedItem: String?,
    onDismissRequest: () -> Unit,
    onItemSelectedListener: (value: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(
            bottom = Dimens.extraLarge
        )
    ) {
        item {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.large),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            VerticalSpacer(height = Dimens.large)
        }
        items(items) { item ->
            val index = items.indexOf(item)
            Row(
                modifier = Modifier
                    .handPointerIcon()
                    .fillMaxWidth()
                    .clickable {
                        onItemSelectedListener(values[index])
                        onDismissRequest()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(vertical = Dimens.large)
                        .padding(start = Dimens.large),
                )
                HorizontalSpacer(width = Dimens.large)
                if (values[index] == selectedItem) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.padding(end = Dimens.large)
                    )
                }
            }
        }
    }
}
