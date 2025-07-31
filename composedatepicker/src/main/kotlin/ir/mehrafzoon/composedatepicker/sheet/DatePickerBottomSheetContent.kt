package ir.mehrafzoon.composedatepicker.sheet

/*
 * Copyright (C) 2025 Mohammad Sadegh Mehrafzoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.FontRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.huri.jcal.JalaliCalendar
import ir.mehrafzoon.composedatepicker.R
import ir.mehrafzoon.composedatepicker.core.component.AppButton
import ir.mehrafzoon.composedatepicker.core.component.PersianDatePickerController
import ir.mehrafzoon.composedatepicker.lineardatepicker.PersianDatePicker
import ir.mehrafzoon.composedatepicker.lineardatepicker.PersianDatePickerWithoutDay
import java.util.Date

@Composable
internal fun DatePickerBottomSheetContent(
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    controller: PersianDatePickerController,
    submitTitle: String = stringResource(R.string.action_submit),
    titleBottomSheet: String = "",
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    unSelectedStyle: TextStyle? = null,
    selectedStyle: TextStyle? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    lineColor: Color = contentColorFor(backgroundColor),
    @FontRes font: Int? = null,
    onDateChanged: ((year: Int, month: Int, day: Int) -> Unit)? = null,
    onDismissRequest: () -> Unit,
    onSubmitClick: () -> Unit,
    datePickerWithoutDay: Boolean = false,
    useInitialDate: Boolean = false,
    initialDate: Triple<Int, Int, Int>? = null
) {

    val unSelected = unSelectedStyle ?: TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface
    )

    val selected = selectedStyle ?: TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    val recomposeToggleState = remember { mutableStateOf(false) }
    LaunchedEffect(recomposeToggleState.value) {}

    val tmpController by remember(key1 = controller) {
        mutableStateOf(PersianDatePickerController())
    }

    LaunchedEffect(controller) {
        if (useInitialDate && initialDate != null) {
            val persianDate = JalaliCalendar(
                initialDate.first,
                initialDate.second,
                initialDate.third
            )
            val gregorianDate = persianDate.toGregorian().time
            tmpController.updateDate(gregorianDate)
        } else {
            tmpController.updateDate(Date(controller.date.toGregorian().timeInMillis))
        }
    }

    Surface(
        modifier = modifier
    ) {

        Text(
            text = titleBottomSheet,
            modifier = titleModifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = titleStyle,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                if (datePickerWithoutDay) {
                    PersianDatePickerWithoutDay(
                        controller = tmpController,
                        selectedStyle = selected,
                        unSelectedStyle = unSelected,
                        font = font,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .fillMaxWidth(),
                        onDateChanged = { year, month, day ->
                            if (onDateChanged != null) {
                                onDateChanged(year, month, day)
                            }
                            recomposeToggleState.value = !recomposeToggleState.value
                        },
                    )

                } else {
                    PersianDatePicker(
                        controller = tmpController,
                        contentColor = lineColor,
                        selectedStyle = selected,
                        unSelectedStyle = unSelected,
                        font = font,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .fillMaxWidth(),
                        onDateChanged = { year, month, day ->
                            if (onDateChanged != null) {
                                onDateChanged(year, month, day)
                            }
                            recomposeToggleState.value = !recomposeToggleState.value
                        },
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                AppButton(
                    label = submitTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enable = true,
                    onClick = {
                        val date = Date(tmpController.date.toGregorian().timeInMillis)
                        controller.updateDate(date)

                        recomposeToggleState.value = !recomposeToggleState.value

                        onSubmitClick()

                        onDismissRequest()
                    }
                )
            }
        }
    }
}
