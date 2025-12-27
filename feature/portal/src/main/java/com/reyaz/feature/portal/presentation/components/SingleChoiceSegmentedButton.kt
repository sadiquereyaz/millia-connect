package com.reyaz.feature.portal.presentation.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onSelectedIndexChanged: (AutomationType) -> Unit = {}
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        AutomationType.entries.forEachIndexed { index, automationType ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = AutomationType.entries.size
                ),
                onClick = { onSelectedIndexChanged(automationType) },
                selected = index == selectedIndex,
                label = { Text(automationType.title) }
            )
        }
    }
}

enum class AutomationType(
    val title: String
) {
    WORK_MANAGER("Periodic"),
    FOREGROUND_SERVICE("Instant")
}