package com.reyaz.feature.rent.presentation.property_list_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_list_screen.components.PropertyCard
import com.reyaz.feature.rent.presentation.property_list_screen.components.SearchBar

@Composable
fun PropertyListScreen(
    modifier: Modifier = Modifier,
    viewModel: PropertyListViewModel,
    onDetailClick: (property: Property) -> Unit,
    onAddClick: () -> Unit,
    showSearchComponents: Boolean
) {
    val uiState by viewModel.screenData.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                uiState.error?.let {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 80.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (showSearchComponents) {
                                SearchBar(
                                    searchText = searchQuery,
                                    onSearchChange = { it ->
                                        searchQuery = it
                                        viewModel.onSearchChange(it)
                                    },
                                    onSearchTriggered = {
                                        // viewModel.search()
                                    }
                                )

                            }
                        }
                        items(uiState.filteredList) { it ->
                            PropertyCard(property = it, onDetailClick = { onDetailClick(it) })
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

