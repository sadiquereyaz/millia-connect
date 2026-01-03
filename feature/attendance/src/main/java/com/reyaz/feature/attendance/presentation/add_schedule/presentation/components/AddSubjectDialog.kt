package com.reyaz.feature.attendance.presentation.add_schedule.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit, onConfirm: (String) -> Unit
) {
    var subjectName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text("Add New Subject")
        }
    }, text = {
        Column {
            OutlinedTextField(
                value = subjectName,
                onValueChange = {
                    subjectName = it
                    isError = it.isBlank()
                },
                label = { Text("Subject Name") },
                placeholder = { Text("e.g. Engineering Mathematics") },
                isError = isError,
                supportingText = if (isError) {
                    { Text("Subject name cannot be empty") }
                } else null,
                singleLine = true,
                modifier = Modifier.Companion.fillMaxWidth())
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (subjectName.isNotBlank()) {
                    onConfirm(subjectName.trim())
                } else {
                    isError = true
                }
            }, enabled = subjectName.isNotBlank()
        ) {
            Text("Add")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}