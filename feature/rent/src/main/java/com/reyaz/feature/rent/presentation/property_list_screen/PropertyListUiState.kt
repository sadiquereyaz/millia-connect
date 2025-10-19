package com.reyaz.feature.rent.presentation.property_list_screen

import com.reyaz.feature.rent.domain.model.Property

sealed class PropertyListUiState<out T> {
    object Loading : PropertyListUiState<Nothing>()
    data class Success<T>(val data: T) : PropertyListUiState<T>()
    data class Error(val message: String) : PropertyListUiState<Nothing>()
}

data class PropertyListScreenData(
    val isLoading: Boolean = true,
    val error: String? = null,
    val user: User = User(),  // todo: remove default
    val isAdmin: Boolean = true, // todo: make it false
    val searchText: String = "",
    val filteredList: List<Property> = emptyList(),
    val propertyList: List<Property> = emptyList(),
    val selectedTab: PropertyListScreenTab = PropertyListScreenTab.ALL
){
    val showTabs: Boolean = searchText.isEmpty() && ( isAdmin || propertyList.any { it.ownerName == user.name } )
}

enum class PropertyListScreenTab (val value: String){
    ALL("All"),
    MY_PROPERTY("My Property"),
    MANAGE_PROPERTY("Manage Property")
}

data class User(
    val id: String = "fadff",
    val name: String? = "Sadique Reyaz",
    val imageUrl: String = "https://avatars.githubusercontent.com/u/118601913?s=400&u=752ca858776d252fabc6126797f6aaa3f5e9912a&v=4"
)