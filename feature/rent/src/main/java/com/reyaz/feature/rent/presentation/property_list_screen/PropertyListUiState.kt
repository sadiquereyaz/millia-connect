package com.reyaz.feature.rent.presentation.property_list_screen

import com.reyaz.core.common.utils.orFalse
import com.reyaz.feature.rent.domain.model.Property

data class PropertyListScreenData(
    val isLoading: Boolean = true,
    val error: String? = null,
    val user: User = User(),  // todo: remove default
    val isAdmin: Boolean = true, // todo: make it false
    val searchText: String = "",
    val filteredList: List<Property>? = null,
    val propertyList: List<Property>? = null,
    val selectedTab: PropertyListScreenTab = PropertyListScreenTab.ALL
){
    val feedListProperty: List<Property>? = filteredList ?: propertyList
    val showTabs: Boolean = searchText.isEmpty() && ( isAdmin || propertyList?.any { it.ownerName == user.name }.orFalse() )

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
