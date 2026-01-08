package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.SingleChoiceSegmentedButton

@Composable
fun AutomationSegmentButton(
    onSelected: (Int) -> Unit,
    selectedIndex: Int?
) {
    Column {
        Row(
            modifier = Modifier.Companion.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text("Enable Automation:", fontWeight = FontWeight.Companion.Medium, fontSize = 20.sp)
            SingleChoiceSegmentedButton(
                options = listOf("Yes", "No"),
                onOptionSelect = { onSelected(it) },
                selectedIndex = selectedIndex
            )
        }
        Text(
            "Your attendance will be marked automatically based on the location of the device.",
            fontSize = 14.sp,
            textAlign = TextAlign.Companion.Center,
            fontStyle = FontStyle.Companion.Italic,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.Companion.padding(top = 4.dp).padding(horizontal = 16.dp)
        )
    }
}