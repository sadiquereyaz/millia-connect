package com.reyaz.feature.attendance.presentation.add_schedule.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.components.text_field.CustomCircularTextField
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.formatMinutesToTime
import kotlinx.datetime.LocalTime

@Composable
fun TimeSelector(
    label: String,
    timeMinutes: Int,
    onTimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val cornerRadius = 12
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .clickable(onClick = {
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            val timeInMin = hour * 60 + minute
                            onTimeClick(timeInMin)
                        },
                        timeMinutes / 60,
                        0,
                        false
                    )
                    timePickerDialog.show()
                })
        ) {
            CustomCircularTextField(
                value = formatMinutesToTime(timeMinutes),
                onValueChange = {},
                label = label,
//                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Filled.AccessTime, contentDescription = "Select Time")
                },
                cornerRadius = cornerRadius,
                outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier.Companion.fillMaxWidth()
            )
        }
    }
}