package com.reyaz.feature.result.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// todo: move to core:ui
@Composable
 fun ResultListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
//        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        color = MaterialTheme.colorScheme.outlineVariant
    )
}