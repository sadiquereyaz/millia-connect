package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.SingleLineText
import com.reyaz.core.ui.components.text.DottedUnderlineText

@Composable
fun LocationField(
    modifier: Modifier = Modifier.Companion,
    locationName: String,
    onLocationClick: () -> Unit,
) {
    val gradientBrush = Brush.Companion.linearGradient(
        colors = listOf(
            Color(0xFF6B73FF), Color(0xFF784BA0)
        )
    )

    if (locationName.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    // open map
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
                        maxLine = 1,
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
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "location",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.Companion.size(20.dp)
            )
            Spacer(Modifier.Companion.width(8.dp))
            DottedUnderlineText(
                text = locationName,
                fontSize = 18.sp,
                textColor = MaterialTheme.colorScheme.primary,
                underlineColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    // open mapplse map screen
                }
            )
        }
    }
}