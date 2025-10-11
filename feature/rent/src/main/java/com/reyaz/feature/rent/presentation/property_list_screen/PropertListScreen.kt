package com.reyaz.feature.rent.presentation.property_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_post_screen.components.PropertyCard

@Composable
fun PropertyListScreen(
    modifier: Modifier,
    viewModel: PropertyListViewModel,
    onPostClick:()->Unit,
    onDetailViewClick:()->Unit
){
      val uiState by viewModel.propertiesState.collectAsStateWithLifecycle()

        Column (modifier=Modifier
            .fillMaxSize()){
            Text("ye Property List screen hi hai, uistate me kch problem hai wo tum dekho. property list screen launch hone lagi hai" +
                    ". Jab bhi chatgpt se poochna usko property navgraph aur navigation route wala code de dena. ek bar noticeuistate ka mera code dekh lo. tum ne bht complex bna diya hai property ui state ko. sham ko karte hain clear.")
            when(uiState){
                is PropertyListUiState.Error -> {
                    //when error show error on screen
                    val error = (uiState as PropertyListUiState.Error).message
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        Text(text = error)
                    }
                }
                PropertyListUiState.Loading -> {
                    //when loading show circular progress indicator
                    Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is PropertyListUiState.Success<*> -> {
                    //take out list
                    val list = (uiState as PropertyListUiState.Success<List<Property>>).data
                    LazyColumn (
                        modifier=Modifier.fillMaxSize()
                    ){
                        items(list.size){i->
                            val property = list[i]
                            PropertyCard(property)
                        }
                    }
                }
            }
        }
    }
