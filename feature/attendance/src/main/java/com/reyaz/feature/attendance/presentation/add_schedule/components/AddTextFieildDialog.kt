package com.reyaz.feature.attendance.presentation.add_schedule.components

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
import com.reyaz.feature.attendance.domain.model.AddFieldDialogType

// todo: do not dismiss when user click out of dialog
@Composable
fun AddTextFieildDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: AddFieldDialogType,
    suggestOptions: List<String> = emptyList()
) {
    var fieldValue by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Add New ${type.name}")
            }
        },
        text = {
            Column {
                // todo: it should be auto suggest drop down
                OutlinedTextField(
                    value = fieldValue,
                    onValueChange = {
                        fieldValue = it
                        isError = it.isBlank()
                    },
                    label = { Text("${type.label} Name") },
                    placeholder = { Text("e.g. ${type.label}") },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("${type.name} cannot be empty") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.Companion.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fieldValue.isNotBlank()) {
                        onConfirm(fieldValue.trim())
                    } else {
                        isError = true
                    }
                }, enabled = fieldValue.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}