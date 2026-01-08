package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.text_field.CustomCircularTextField
import com.reyaz.feature.attendance.data.local.model.SubjectEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropdown(
    selectedSubject: String,
    subjects: List<SubjectEntity>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSubjectSelected: (Long) -> Unit,
    onAddNewSubject: () -> Unit,
    imeAction: ImeAction = ImeAction.Companion.Unspecified,
) {
    Column(
        modifier = Modifier.Companion.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = onExpandedChange
        ) {
            CustomCircularTextField(
                value = selectedSubject,
                onValueChange = {},
                cornerRadius = 12,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = "Subject",
                placeholder = "Select Subject",
                modifier = Modifier.Companion.menuAnchor(type = MenuAnchorType.Companion.PrimaryNotEditable),
                imeAction = imeAction
            )
            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(text = subject.name, fontSize = 16.sp) },
                        onClick = { onSubjectSelected(subject.subjectId) })
                }

                if (subjects.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.Companion.padding(vertical = 4.dp))
                }

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.Companion.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "Add New Subject",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            contentDescription = "Add new subject",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }, onClick = onAddNewSubject
                )
            }
        }
    }
}