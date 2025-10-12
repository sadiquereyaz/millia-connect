package com.reyaz.feature.rent.presentation.property_post_screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.reyaz.core.auth.domain.repository.GoogleService
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyPostViewModel(
    private val propertyRepository: PropertyRepository,
    private val googleSign: GoogleService
): ViewModel() {
//this variable is for keeping the track whether use is logged in or not
    private val _user = MutableStateFlow(Firebase.auth.currentUser)
    val user = _user.asStateFlow()

    //ui state for property
    private val _uiState = MutableStateFlow(Property())
    val uiState = _uiState.asStateFlow()

    //this viewmodel will be responsible for only posting the property
    fun postProperty(property: Property) {
        viewModelScope.launch {
            propertyRepository
                .postProperty(property)
                .onSuccess {
                    Log.d("success", "success")
                }
                .onFailure {
                    Log.d("error", it.message.toString())
                }
        }
    }

    fun doGoogleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        login: () -> Unit
        ){
        googleSign.googleSignIn(
            context = context,
            scope = scope,
            launcher = launcher,
            login = login
        )
    }


    //all function related to ui changes
    fun onPropertyTitleChange(newTitle:String){
        _uiState.update {
            it.copy(propertyTitle = newTitle)
        }
    }
    fun onPropertyTypeChange(newType:String){
        _uiState.update {
            it.copy(propertyType = newType)
        }
    }
    fun onBhkChange(newBHK:String) {
        _uiState.update {
            it.copy(propertyBHK = newBHK)
        }
    }
    fun onFloorNumberChange(newFloor:String){
        _uiState.update {
            it.copy(propertyFloorNumber = newFloor)
        }
    }
    fun onTotalFloorChange(newTotalFloor:String){
        _uiState.update {
            it.copy(totalFloor = newTotalFloor)
        }
    }
    fun onRentChange(newRent:String){
        _uiState.update {
            it.copy(propertyRent = newRent)
        }
    }
    fun onSecurityDepositChange(newSecurityDeposit:String){
        _uiState.update {
            it.copy(securityDeposit = newSecurityDeposit)
        }
    }
    fun onDescriptionChange(newDescription:String){
        _uiState.update {
            it.copy(propertyDescription = newDescription)
        }
    }
    fun onAmenityChange(amenity: List<String>) {
        _uiState.update {
            it.copy(amenities = amenity)
        }
    }
    fun onLocationChange(newLocation:String){
        _uiState.update {
            it.copy(propertyLocation = newLocation)
        }
    }
}