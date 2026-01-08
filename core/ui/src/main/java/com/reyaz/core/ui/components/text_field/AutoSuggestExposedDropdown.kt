package com.reyaz.core.ui.components.text_field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoSuggestExposedDropdown(
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    placeholderText: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }

    val filtered = suggestions.filter {
        it.contains(value, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { newExpanded ->
            // Only allow manual expansion/collapse, not automatic keyboard dismissal
            if (newExpanded) {
                expanded = true
            }
        }
    ) {
        CustomSlimTextField(
            value = value,
            onValueChange = {
                expanded = true
                onValueChange(it)
            },
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            label = labelText,
            placeholder = placeholderText,
            supportingText = supportingText,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && filtered.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filtered.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            expanded = false
                            onValueChange(it)
                        }
                    )
                }
        }
    }
}
