package com.reyaz.core.ui.components.text_field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CustomCircularTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
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
    imeAction: ImeAction = ImeAction.Unspecified,
    capitalisation: KeyboardCapitalization = KeyboardCapitalization.Sentences
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder?.let { { Text(placeholder, color = MaterialTheme.colorScheme.outline) } },
        label = label?.let { { Text(it, color = MaterialTheme.colorScheme.outline) } },
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        enabled = enabled,
        isError = isError,
        colors = outlinedTextFieldColors,
        supportingText = supportingText?.let { text ->
            {
                Text(
                    text = text,
                    color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        },
        maxLines = maxLines,
        shape = cornerRadius?.let { RoundedCornerShape(it.dp) } ?: RoundedCornerShape(50),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            capitalization = capitalisation
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
            onNext = { focusManager.clearFocus() },
            onGo = { focusManager.clearFocus() },
            onSearch = { focusManager.clearFocus() }
        )
    )
}

@Preview(showBackground = true, name = "Circular Text Field States")
@Composable
fun CircularTextFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Empty State (Shows Placeholder if focused, or just Label)
            CustomCircularTextField(
                value = "",
                onValueChange = {},
                label = "Username",
                placeholder = "Enter your username"
            )

            // 2. Filled State
            CustomCircularTextField(
                value = "JohnDoe123",
                onValueChange = {},
                label = "Username",
            )

            // 3. Disabled State
            CustomCircularTextField(
                value = "This is value",
                onValueChange = {},
                label = "label it is",
                cornerRadius = 16
            )
            // 4. Disabled State
            CustomCircularTextField(
                value = "Locked Value",
                onValueChange = {},
                label = "Account ID",
                enabled = false
            )
        }
    }
}