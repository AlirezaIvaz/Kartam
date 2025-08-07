package ir.alirezaivaz.kartam.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.ChangelogItem
import ir.alirezaivaz.kartam.dto.ChangelogType
import ir.alirezaivaz.kartam.dto.ChangelogVersion
import org.xmlpull.v1.XmlPullParser

fun Context.parseChangelog(): List<ChangelogVersion> {
    val changelog = mutableListOf<ChangelogVersion>()
    val parser = resources.getXml(R.xml.changelog)

    var event = parser.eventType
    var currentVersion: ChangelogVersion? = null
    var currentItems = mutableListOf<ChangelogItem>()

    while (event != XmlPullParser.END_DOCUMENT) {
        when (event) {
            XmlPullParser.START_TAG -> {
                when (parser.name) {
                    "version" -> {
                        val versionName = parser.getAttributeValue(null, "name") ?: ""
                        val date = parser.getAttributeValue(null, "date") ?: ""
                        currentItems = mutableListOf()
                        currentVersion = ChangelogVersion(versionName, date, currentItems)
                    }

                    else -> {
                        val type = ChangelogType.fromTag(parser.name)
                        if (type != null) {
                            val description = parser.nextText()
                            currentItems.add(ChangelogItem(type, description))
                        }
                    }
                }
            }

            XmlPullParser.END_TAG -> {
                if (parser.name == "version") {
                    currentVersion?.let { changelog.add(it) }
                }
            }
        }
        event = parser.next()
    }

    return changelog
}

@Composable
fun buildChangelogAnnotatedString(type: ChangelogType, description: String): AnnotatedString {
    val rawText = stringResource(type.labelRes, description)

    val annotatedString = buildAnnotatedString {
        if (type == ChangelogType.INFO) {
            append(rawText)
            return@buildAnnotatedString
        }

        val pattern = Regex("""\*\*(.+?)\*\*""")
        var currentIndex = 0

        for (match in pattern.findAll(rawText)) {
            if (match.range.first > currentIndex) {
                append(rawText.substring(currentIndex, match.range.first))
            }

            val boldText = match.groupValues[1]
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(type.colorRes)
                )
            ) {
                append(boldText)
            }

            currentIndex = match.range.last + 1
        }

        if (currentIndex < rawText.length) {
            append(rawText.substring(currentIndex))
        }
    }

    return annotatedString
}
