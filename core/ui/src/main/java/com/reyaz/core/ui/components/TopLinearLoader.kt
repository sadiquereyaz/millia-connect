package com.reyaz.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopLinearLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        if (isLoading)
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
    }
}