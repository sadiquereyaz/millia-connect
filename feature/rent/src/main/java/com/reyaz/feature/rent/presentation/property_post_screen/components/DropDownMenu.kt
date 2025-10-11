package com.reyaz.feature.rent.presentation.property_post_screen.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(option:List<String>,onSelect:(String)->Unit,label: String,modifier: Modifier){

//    val options = listOf("1 BHK", "2 BHK", "3 BHK", "Shared Room")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(option[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded } ,
        modifier=modifier// toggle menu
    ) {
        // The text field (read-only)
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .border(
                    1.dp,Color.Gray
                )
                .menuAnchor()
                .fillMaxWidth()// 👈 required for dropdown to align
            ,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background, // 👈 background same as screen
                unfocusedContainerColor =MaterialTheme.colorScheme.background,
                disabledContainerColor =MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent,  // 👈 remove default underline
                unfocusedIndicatorColor = Color.Transparent,
            )

        )

        // The dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            option.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}