package com.reyaz.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [warning] don't use it when composable containing it is clickable
 * */
@Composable
fun SingleLinePopText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    isUnderline: Boolean = false,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    shouldShowPopup: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    var showPopup by remember { mutableStateOf(false) }
    var isTextOverflowing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box {
        Text(
            text = text,
            modifier = modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick?.invoke()
                    },
                    onPress = {
                        showPopup = true
                        tryAwaitRelease()
                        // Keep showing for 1 second after release
                        delay(500)
                        showPopup = false
                    }
                )
            },
            textAlign = textAlign,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            lineHeight = lineHeight,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            textDecoration = if (isUnderline) TextDecoration.Underline else null,
            fontStyle = fontStyle,
            onTextLayout = { textLayoutResult ->
                isTextOverflowing = textLayoutResult.hasVisualOverflow
            }
        )

        if (showPopup && shouldShowPopup && isTextOverflowing) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { showPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                        .widthIn(max = 280.dp)
                ) {
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}
