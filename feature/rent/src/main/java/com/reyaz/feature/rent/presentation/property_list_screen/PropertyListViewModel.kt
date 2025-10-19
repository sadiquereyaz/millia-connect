package com.reyaz.feature.rent.presentation.property_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyListViewModel(
    private val propertyRepository: PropertyRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _screenData = MutableStateFlow(PropertyListScreenData())
    val screenData = _screenData.asStateFlow()

    init {
        getCurrentUser()
        getAllProperty()
    }

    private fun getAllProperty() {
        viewModelScope.launch(Dispatchers.IO) {
            propertyRepository
                .getAllProperty()
                .catch { e ->
                    Log.d("error", e.message.toString())
                    _screenData.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { properties ->
                    _screenData.update {
                        it.copy(
                            isLoading = false,
                            propertyList = properties,
                            filteredList = properties
                        )
                    }
                }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                firebaseAuth.currentUser?.let {
                    val user = User(
                        id = it.uid,
                        name = it.displayName,
                        imageUrl = it.photoUrl.toString()
                    )
                    _screenData.update {
                        it.copy(user = user)
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun onSearchChange(query: String) {
        viewModelScope.launch (Dispatchers.Default){
            val filteredProperty = screenData.value.propertyList.filter { property ->
                property.propertyTitle.contains(query, true) ||
                        property.propertyBHK.contains(query, true) ||
                        property.propertyLocation.contains(query, true)
            }
            _screenData.update {
                it.copy(
                    filteredList = filteredProperty
                )
            }
        }
    }

    fun onTabSelect(tab: PropertyListScreenTab){
        viewModelScope.launch (Dispatchers.Default){
            val filteredProperty = screenData.value.propertyList.filter { property ->
                when(tab){
                    PropertyListScreenTab.ALL -> true
                    PropertyListScreenTab.MY_PROPERTY -> property.ownerName == screenData.value.user.name
                    PropertyListScreenTab.MANAGE_PROPERTY -> property.reportCount > 0
                }
            }
            _screenData.update {
                it.copy(
                    selectedTab = tab,
                    filteredList = filteredProperty
                )
            }
        }
    }
}