package com.reyaz.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItemWithTrailingIcon(
    textWithIndicator: Pair<AnnotatedString, Map<String, InlineTextContent>>,
    date: String?,
    trailingIcon: @Composable() (() -> Unit)?,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null, // Add this parameter
) {
//    val textWithIndicator = textWithIndicator(listTitle, isNewItem)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick // Add long press support
                )
                .padding( 12.dp, 12.dp, 0.dp, 12.dp),
        ) {
            // result list title
            Text(
                text = textWithIndicator.first,
                inlineContent = textWithIndicator.second,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )

            // result list item date
            date?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        // trailing icon
        trailingIcon?.let { it() }
    }
    CustomListDivider(modifier= Modifier)
}
