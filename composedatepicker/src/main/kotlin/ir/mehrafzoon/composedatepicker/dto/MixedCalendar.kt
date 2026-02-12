package ir.mehrafzoon.composedatepicker.dto

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import ir.huri.jcal.JalaliCalendar
import ir.mehrafzoon.composedatepicker.R
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

data class MixedCalendar(
    val year: Int,
    val yearJalali: Int,
    val month: Int,
    val monthJalali: Int,
    val day: Int,
    val dayJalali: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val timestamp: Long,
)

@Composable
fun MixedCalendar.getMonthName(): String {
    val resources = LocalResources.current
    val months = resources.getStringArray(R.array.gregorian_months)
    return months[month - 1]
}

@Composable
fun MixedCalendar.getJalaliMonthName(): String {
    val resources = LocalResources.current
    val months = resources.getStringArray(R.array.jalali_months)
    return months[monthJalali - 1]
}

@Composable
fun MixedCalendar.toPrettyFormatted(isJalali: Boolean = false): String {
    return if (isJalali) {
        toJalaliPrettyFormatted()
    } else {
        toGregorianPrettyFormatted()
    }
}

@Composable
fun MixedCalendar.toGregorianPrettyFormatted(): String = stringResource(
    R.string.formatter_backup_time,
    day,
    getMonthName(),
    year,
    hour,
    minute,
    second,
)

@Composable
fun MixedCalendar.toJalaliPrettyFormatted(): String = stringResource(
    R.string.formatter_backup_time,
    dayJalali,
    getJalaliMonthName(),
    yearJalali,
    hour,
    minute,
    second,
)

fun Long.toMixedCalendar(): MixedCalendar {
    val gregorian = GregorianCalendar().apply {
        time = Date(this@toMixedCalendar)
    }
    val jalali = JalaliCalendar(gregorian)
    val monthGregorian = gregorian.get(Calendar.MONTH) + 1
    val jalaliTime = MixedCalendar(
        yearJalali = jalali.year,
        year = gregorian.get(Calendar.YEAR),
        monthJalali = jalali.month,
        month = monthGregorian,
        dayJalali = jalali.day,
        day = gregorian.get(Calendar.DAY_OF_MONTH),
        hour = gregorian.get(Calendar.HOUR_OF_DAY),
        minute = gregorian.get(Calendar.MINUTE),
        second = gregorian.get(Calendar.SECOND),
        timestamp = this
    )
    return jalaliTime
}
