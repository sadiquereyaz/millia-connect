package com.reyaz.core.ui.components.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MCOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(100)
) {
    val horizontalGradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.inversePrimary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
        )
    )
    val disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    val disabledBorderColor = ButtonDefaults.buttonColors().disabledContainerColor

    Button(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(),
        contentPadding = PaddingValues(), // 1. Remove standard padding
        onClick = onClick,
        shape = shape,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = if (enabled) horizontalGradient else SolidColor(ButtonDefaults.buttonColors().disabledContainerColor),
                    shape = shape
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if(enabled) MaterialTheme.colorScheme.onSurface else Color.Unspecified,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, name = "Gradient Button Preview")
@Composable
fun MCOutlineButtonPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Standard State
            MCOutlineButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Gradient Button",
                onClick = {}
            )

            // Enabled State check
            MCOutlineButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Disabled State",
                enabled = false,
                onClick = {}
            )
        }
    }
}

