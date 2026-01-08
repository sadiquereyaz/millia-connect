package com.reyaz.feature.attendance.presentation.add_schedule.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.components.text_field.CustomCircularTextField
import com.reyaz.core.ui.helper.debounceClickable
import com.reyaz.feature.attendance.utils.TimeUtils
import com.reyaz.feature.attendance.utils.TimeUtils.formatMinutesToTime

@Composable
fun TimeSelector(
    label: String,
    timeMinutes: Int?,
    onTimeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    isError: Boolean = false,
) {
    val context = LocalContext.current

    val cornerRadius = 12
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .debounceClickable(
                    enabled = isClickable,
                    onClick = {
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            val timeInMin = hour * 60 + minute
                            onTimeClick(timeInMin)
                        },
                        timeMinutes?.div(60) ?: TimeUtils.currentHourOfDay,
                        0,
                        false
                    )
                    timePickerDialog.show()
                })
        ) {
            CustomCircularTextField(
                value = timeMinutes?.let {formatMinutesToTime(it)} ?: "",
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
                    disabledBorderColor = if(isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
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