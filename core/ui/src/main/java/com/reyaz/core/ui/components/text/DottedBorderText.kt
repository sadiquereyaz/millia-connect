package com.reyaz.core.ui.components.text


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DottedBorderText(
    text: String,
    fontWeight: FontWeight = FontWeight.SemiBold,
    fontSize: TextUnit = 16.sp,
    textColor: Color = Color.Unspecified,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .dottedBorder(
                color = outlineColor,
                strokeWidth = 1.dp,
                cornerRadius = 12.dp,
                dashOn = 10f,
                dashOff = 8f
            )
            .padding(8.dp)
    ) {
        Text(text = text, fontWeight = fontWeight, fontSize = fontSize, color = textColor)
    }
}

@Preview(showBackground = true, name = "Dotted Border Text Preview")
@Composable
fun DottedBorderTextPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Short text
            DottedBorderText(
                text = "Short Label"
            )

            // Medium text
            DottedBorderText(
                text = "This is a medium length text"
            )

            // Long text
            DottedBorderText(
                text = "This is a longer text that demonstrates how the dotted border wraps around larger content"
            )

            // Tag-like usage
            DottedBorderText(
                text = "Status: Active"
            )

            // Number display
            DottedBorderText(
                text = "Count: 42"
            )
        }
    }
}

@Preview(showBackground = true, name = "Dotted Border Modifier Preview")
@Composable
fun DottedBorderModifierPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Using the extension function on Box
            Box(
                modifier = Modifier
                    .dottedBorder()
                    .padding(12.dp)
            ) {
                Text("Box with dotted border")
            }

            // Custom color and stroke width
            Box(
                modifier = Modifier
                    .dottedBorder(
                        color = Color.Blue,
                        strokeWidth = 2.dp,
                        cornerRadius = 16.dp
                    )
                    .padding(12.dp)
            ) {
                Text("Custom blue border", color = Color.Blue)
            }

            // Custom dash pattern
            Box(
                modifier = Modifier
                    .dottedBorder(
                        color = MaterialTheme.colorScheme.primary,
                        dashOn = 15f,
                        dashOff = 5f,
                        cornerRadius = 8.dp
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Text("Custom dash pattern", fontWeight = FontWeight.Bold)
                    Text("dashOn: 15f, dashOff: 5f", fontSize = 12.sp)
                }
            }

            // No corner radius (sharp corners)
            Box(
                modifier = Modifier
                    .dottedBorder(
                        color = Color.Red,
                        cornerRadius = 0.dp
                    )
                    .padding(12.dp)
            ) {
                Text("Sharp corners (radius: 0)")
            }
        }
    }
}
