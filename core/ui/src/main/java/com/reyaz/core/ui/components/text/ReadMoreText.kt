package com.reyaz.core.ui.components.text
import androidx.compose.foundation.clickable
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun ReadMoreText( // todo: test
    modifier: Modifier = Modifier,
    text: String,
    collapsedMaxLines: Int,
    textStyle: TextStyle = LocalTextStyle.current,
    readMoreText: String = " Read more",
    readLessText: String = " Read less"
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Text(
        text = buildAnnotatedString {
            append(text)
            if (isOverflowing) {
                append(
                    if (isExpanded) readLessText else readMoreText
                )
            }
        },
        color = Color.White.copy(alpha = 0.95f),
        fontSize = 16.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Justify,
        modifier = modifier
            .clickable(enabled = isOverflowing) {
                isExpanded = !isExpanded
            },
        style = textStyle,
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { result ->
            if (!isExpanded) {
                isOverflowing = result.hasVisualOverflow
            }
        }
    )
}
