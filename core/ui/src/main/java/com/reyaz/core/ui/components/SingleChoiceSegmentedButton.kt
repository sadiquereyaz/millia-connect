package com.reyaz.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    options: List<String>,
    onOptionSelect: (Int) -> Unit
) {

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onOptionSelect(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Preview(showBackground = true, name = "Single Choice Segmented Button Preview")
@Composable
fun SingleChoiceSegmentedButtonPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Two options
            var selectedIndex1 by remember { mutableIntStateOf(0) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Two Options:", style = MaterialTheme.typography.labelMedium)
                SingleChoiceSegmentedButton(
                    selectedIndex = selectedIndex1,
                    options = listOf("Option 1", "Option 2"),
                    onOptionSelect = { selectedIndex1 = it }
                )
            }

            // Three options
            var selectedIndex2 by remember { mutableIntStateOf(1) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Three Options:", style = MaterialTheme.typography.labelMedium)
                SingleChoiceSegmentedButton(
                    selectedIndex = selectedIndex2,
                    options = listOf("Day", "Week", "Month"),
                    onOptionSelect = { selectedIndex2 = it }
                )
            }

            // Four options
            var selectedIndex3 by remember { mutableIntStateOf(2) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Four Options:", style = MaterialTheme.typography.labelMedium)
                SingleChoiceSegmentedButton(
                    selectedIndex = selectedIndex3,
                    options = listOf("All", "Active", "Pending", "Done"),
                    onOptionSelect = { selectedIndex3 = it }
                )
            }

            // No selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("No Selection:", style = MaterialTheme.typography.labelMedium)
                SingleChoiceSegmentedButton(
                    selectedIndex = null,
                    options = listOf("Yes", "No", "Maybe"),
                    onOptionSelect = { }
                )
            }
        }
    }
}