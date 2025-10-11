package com.reyaz.feature.rent.presentation.property_detail_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.reyaz.feature.rent.domain.model.Property

@Composable
fun PropertyDetailScreen(property: Property){
    Column(modifier = Modifier.fillMaxSize()){
        Text(text=property.name)
        Text(text=property.age)
    }
}