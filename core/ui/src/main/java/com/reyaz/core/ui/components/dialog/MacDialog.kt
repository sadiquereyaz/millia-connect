package com.reyaz.core.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MacDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    onDismiss: () -> Unit = {},
    dialogContent: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
            ) {
                HorizontalDivider(
                    thickness = 1.5.dp,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.outline
                )
                dialogContent()
            }
            // close icon
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onDismiss() }
                    .padding(8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error),
                imageVector = Icons.Default.Clear, contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onError
            )
            // dialog headline
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(4.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}