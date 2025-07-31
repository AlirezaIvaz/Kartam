package ir.alirezaivaz.kartam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.alirezaivaz.kartam.R

val kodeMonoFontFamily = FontFamily(
    Font(
        resId = R.font.kodemono_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.kodemono_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.kodemono_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.kodemono_semi_bold,
        weight = FontWeight.SemiBold
    ),
)

val aradFontFamily = FontFamily(
    Font(
        resId = R.font.arad_thin,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.arad_extra_light,
        weight = FontWeight.ExtraLight
    ),
    Font(
        resId = R.font.arad_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.arad_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.arad_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.arad_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.arad_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.arad_extra_bold,
        weight = FontWeight.ExtraBold
    ),
)

val aradTypography = Typography().run {
    val arad = aradFontFamily
    copy(
        displayLarge = displayLarge.copy(fontFamily = arad),
        displayMedium = displayMedium.copy(fontFamily = arad),
        displaySmall = displaySmall.copy(fontFamily = arad),
        headlineLarge = headlineLarge.copy(fontFamily = arad),
        headlineMedium = headlineMedium.copy(fontFamily = arad),
        headlineSmall = headlineSmall.copy(fontFamily = arad),
        titleLarge = titleLarge.copy(fontFamily = arad),
        titleMedium = titleMedium.copy(fontFamily = arad),
        titleSmall = titleSmall.copy(fontFamily = arad),
        bodyLarge = bodyLarge.copy(fontFamily = arad),
        bodyMedium = bodyMedium.copy(fontFamily = arad),
        bodySmall = bodySmall.copy(fontFamily = arad),
        labelLarge = labelLarge.copy(fontFamily = arad),
        labelMedium = labelMedium.copy(fontFamily = arad),
        labelSmall = labelSmall.copy(fontFamily = arad),
    )
}

val montserratFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_thin,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.montserrat_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.montserrat_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.montserrat_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.montserrat_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.montserrat_extra_bold,
        weight = FontWeight.ExtraBold
    ),
)

val montserratTypography = Typography().run {
    val montserrat = montserratFontFamily
    copy(
        displayLarge = displayLarge.copy(fontFamily = montserrat),
        displayMedium = displayMedium.copy(fontFamily = montserrat),
        displaySmall = displaySmall.copy(fontFamily = montserrat),
        headlineLarge = headlineLarge.copy(fontFamily = montserrat),
        headlineMedium = headlineMedium.copy(fontFamily = montserrat),
        headlineSmall = headlineSmall.copy(fontFamily = montserrat),
        titleLarge = titleLarge.copy(fontFamily = montserrat),
        titleMedium = titleMedium.copy(fontFamily = montserrat),
        titleSmall = titleSmall.copy(fontFamily = montserrat),
        bodyLarge = bodyLarge.copy(fontFamily = montserrat),
        bodyMedium = bodyMedium.copy(fontFamily = montserrat),
        bodySmall = bodySmall.copy(fontFamily = montserrat),
        labelLarge = labelLarge.copy(fontFamily = montserrat),
        labelMedium = labelMedium.copy(fontFamily = montserrat),
        labelSmall = labelSmall.copy(fontFamily = montserrat),
    )
}
