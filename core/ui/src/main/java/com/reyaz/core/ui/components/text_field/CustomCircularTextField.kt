package com.reyaz.core.ui.components.text_field

// Add these imports if they are missing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@Composable
fun CustomCircularTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    enabled: Boolean = true,
    supportingText: String? = null,
    isError: Boolean = false,
    maxLines: Int = 1,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder?.let { { Text(placeholder) } },
        label = { Text(label) },
        enabled = enabled,
        isError = isError,
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        },
        maxLines = maxLines
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
                value = "Locked Value",
                onValueChange = {},
                label = "Account ID",
                enabled = false
            )
        }
    }
}