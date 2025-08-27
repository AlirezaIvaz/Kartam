package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R

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
        modifier = Modifier
    ) {
        item {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        }
        items(items) { item ->
            val index = items.indexOf(item)
            Row(
                modifier = Modifier
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
                        .padding(vertical = dimensionResource(R.dimen.padding_vertical))
                        .padding(start = dimensionResource(R.dimen.padding_horizontal)),
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_horizontal)))
                if (values[index] == selectedItem) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_horizontal))
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}
