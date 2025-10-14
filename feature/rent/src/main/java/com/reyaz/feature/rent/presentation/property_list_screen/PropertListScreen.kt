package com.reyaz.feature.rent.presentation.property_list_screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_list_screen.components.PropertyCard

@Composable
fun PropertyListScreen(
    modifier: Modifier = Modifier,
    viewModel: PropertyListViewModel,
    onDetailClick: (property: Property) -> Unit,
    onAddClick: () -> Unit,
    showSearchComponents : Boolean
) {
    val uiState by viewModel.propertiesState.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is PropertyListUiState.Error -> {
                    val error = (uiState as PropertyListUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = error)
                    }
                }

                is PropertyListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is PropertyListUiState.Success<*> -> {
                    val list = (uiState as PropertyListUiState.Success<List<Property>>).data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            if(showSearchComponents) {
                                Text("Header")
                            }
                        }
                        items(list.size) { index ->
                            val property = list[index]
                            PropertyCard(property, onDetailClick)
                        }
                    }
                }
            }
        }


        FloatingActionButton(
            onClick = onAddClick,
            containerColor = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Property",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

