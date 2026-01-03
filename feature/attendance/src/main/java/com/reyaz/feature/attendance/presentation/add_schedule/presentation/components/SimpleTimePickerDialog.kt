package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleTimePickerDialog(
    initialMinutes: Int, onTimeSelected: (Int) -> Unit, onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialMinutes / 60) }
    var minute by remember { mutableStateOf(initialMinutes % 60) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Select Time") }, text = {
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            // Hour selector
            Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                IconButton(onClick = { hour = (hour + 1) % 24 }) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase hour")
                }
                Text(
                    text = String.format("%02d", hour),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
                IconButton(onClick = { hour = if (hour == 0) 23 else hour - 1 }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease hour")
                }
            }

            Text(
                text = ":",
                fontSize = 32.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion.padding(horizontal = 8.dp)
            )

            // Minute selector
            Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
                IconButton(onClick = { minute = (minute + 5) % 60 }) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase minute")
                }
                Text(
                    text = String.format("%02d", minute),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
                IconButton(onClick = { minute = if (minute < 5) 55 else minute - 5 }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "Decrease minute"
                    )
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = {
            onTimeSelected(hour * 60 + minute)
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}