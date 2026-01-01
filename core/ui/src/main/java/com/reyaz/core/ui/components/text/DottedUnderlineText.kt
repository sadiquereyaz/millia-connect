package com.reyaz.core.ui.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DottedUnderlineText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    underlineColor: Color = MaterialTheme.colorScheme.outline,
    fontSize: TextUnit = 16.sp,
    maxLines: Int = Int.MAX_VALUE,
    onClick: (() -> Unit)? = null,
) {
    Text(
        text = text,
        color = textColor,
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth + 2f

                drawLine(
                    color = underlineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 8f), // dot width, gap
                        0f
                    )
                )
            }
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
    )
}

@Preview(showBackground = true, name = "Dotted Underline Text Preview")
@Composable
fun DottedUnderlineTextPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Default style
            DottedUnderlineText(
                text = "Default Dotted Underline"
            )

            // With custom colors
            DottedUnderlineText(
                text = "your Custom Colors",
                textColor = Color.Blue,
                underlineColor = Color.Red
            )

            // With custom font size
            DottedUnderlineText(
                text = "Large Text",
                fontSize = 24.sp,
                textColor = Color(0xFF6200EE),
                underlineColor = Color(0xFF03DAC5)
            )

            // Small text
            DottedUnderlineText(
                text = "Small dotted text",
                fontSize = 12.sp,
                textColor = Color.Gray,
                underlineColor = Color.LightGray
            )
        }
    }
}
