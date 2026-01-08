package com.reyaz.core.ui.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomSlimTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    supportingText: String? = null,
    isError: Boolean = false,
    maxLines: Int = 1,
    cornerRadius: Int? = null,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    outlinedTextFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    // Sync external value changes
    val textFieldValue = if (textFieldValueState.text != value) {
        TextFieldValue(text = value, selection = TextRange(value.length))
    } else {
        textFieldValueState
    }

    val shape = cornerRadius?.let { RoundedCornerShape(it.dp) } ?: CircleShape

    Column(modifier = modifier.fillMaxWidth()) {

        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValueState = newValue
                onValueChange(newValue.text)
            },
            enabled = enabled,
            readOnly = readOnly,
            maxLines = maxLines,
            singleLine = maxLines == 1,
            cursorBrush = SolidColor(outlinedTextFieldColors.cursorColor),
            textStyle = TextStyle(
                color = if (enabled)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = keyboardActions,
            modifier = Modifier
                .onFocusChanged { isFocused = it.isFocused }
                .fillMaxWidth()
                .clip(shape)
                .background(outlinedTextFieldColors.unfocusedContainerColor)
                .border(
                    width = 1.5.dp,
                    color = when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocused -> MaterialTheme.colorScheme.primary
                        else -> outlinedTextFieldColors.unfocusedIndicatorColor
                    },
                    shape = shape
                )
                .padding(vertical = 10.dp)
        ) { innerTextField ->

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                leadingIcon?.invoke()

                Box(modifier = Modifier.weight(1f)) {

                    if (textFieldValue.text.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder ?: label.orEmpty(),
                            color = outlinedTextFieldColors.unfocusedPlaceholderColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    innerTextField()
                }

                if (textFieldValue.text.isNotEmpty() && enabled && !readOnly) {
                    trailingIcon?.invoke() ?: Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .clickable {
                                textFieldValueState = TextFieldValue("")
                                onValueChange("")
                            }
                    )
                }
            }
        }

        if (supportingText != null) {
            Text(
                text = supportingText,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
