package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.reyaz.core.common.utils.extensions.StringUtils.capitalizeWordLevel
import com.reyaz.core.ui.components.dialog.MacDialog
import com.reyaz.core.ui.components.text_field.AutoSuggestExposedDropdown
import com.reyaz.feature.attendance.domain.model.AddFieldDialogType

@Composable
fun AddTextFieldDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: AddFieldDialogType,
    suggestOptions: List<String>
) {
    var fieldValue by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    MacDialog(
        title = "Add New ${type.displayName}",
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            AutoSuggestExposedDropdown(
                suggestions = suggestOptions,
                value = fieldValue,
                onValueChange = {
                    fieldValue = it
                    isError = it.isBlank()
                },
                labelText = "${type.displayName} Name",
                placeholderText = "e.g. ${type.label}",
                isError = isError,
                supportingText = if (isError) "${type.displayName} cannot be empty" else null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val notPresent = suggestOptions.none { it.equals(fieldValue, ignoreCase = true) }
                    if (fieldValue.isNotBlank()) {
                        if (notPresent)
                            onConfirm(fieldValue.trim().capitalizeWordLevel())
                        onDismiss()
                    } else {
                        isError = true
                    }
                },
                enabled = fieldValue.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}