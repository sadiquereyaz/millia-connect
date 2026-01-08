package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.SingleLineText
import com.reyaz.core.ui.components.text.DottedUnderlineText
import com.reyaz.feature.attendance.domain.model.LocationModel

@Composable
fun LocationField(
    modifier: Modifier = Modifier,
    selectedId: Long?,
    locationList: List<LocationModel>,
    onLocationSelect: (Long) -> Unit,
    navigateToPicker: () -> Unit
) {
    val gradientBrush = Brush.Companion.linearGradient(
        colors = listOf(
            Color(0xFF6B73FF), Color(0xFF784BA0)
        )
    )
    if (locationList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    // open map
                    navigateToPicker()
                }
                .background(gradientBrush)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.Companion.fillMaxSize(),
                verticalAlignment = Alignment.Companion.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location",
                    tint = Color.Companion.White.copy(alpha = 0.9f),
                    modifier = Modifier.Companion.size(28.dp)
                )
                Spacer(Modifier.Companion.width(16.dp))
                Column(
                    modifier = Modifier.Companion.weight(1f)
                ) {
                    SingleLineText(
                        modifier = Modifier.Companion.padding(end = 16.dp),
                        text = "Locate Classroom",
                        maxLines = 1,
                        fontSize = 18.sp,
                        color = Color.Companion.White,
                        fontWeight = FontWeight.Companion.Bold
                    )
                    SingleLineText(
                        text = "powered by Mappls",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.Companion.White,
                        fontStyle = FontStyle.Companion.Italic,

                        )
                }

                Icon(
                    modifier = Modifier.Companion
                        .clip(CircleShape)
                        .size(32.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Companion.White.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "location",
                    tint = Color.Companion.White.copy(alpha = 0.9f),
                )
            }
        }
    } else {
        Column {
            Text(
                "Select Location:",
                fontWeight = FontWeight.Companion.Medium, fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
            FlowRow(
                maxItemsInEachRow = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
//            verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                locationList.forEach { location ->
                    FilterChip(
                        selected = location.id == selectedId,
                        onClick = {
                            onLocationSelect(location.id)
                        },
                        label = {
                            Text(
                                text = location.name,
                                modifier = Modifier
                            )
                        },
                        leadingIcon = if (location.id == selectedId) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                DottedUnderlineText(
                    text = "Add More Location",
                    fontSize = 18.sp,
                    textColor = MaterialTheme.colorScheme.primary,
                    underlineColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        // open mapplse map screen
                        navigateToPicker()
                    }
                )
                Spacer(Modifier.Companion.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}