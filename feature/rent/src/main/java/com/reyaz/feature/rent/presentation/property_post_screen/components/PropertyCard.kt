package com.reyaz.feature.rent.presentation.property_post_screen.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyaz.feature.rent.domain.model.Property

@Composable
fun PropertyCard(property: Property){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 8.dp, end = 8.dp,top=8.dp),
        border = BorderStroke(1.dp,Color.Gray),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =Color.Unspecified
        )
    ){
        Column(modifier=Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly){
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text=property.title,
                    fontWeight = FontWeight.SemiBold,
                    modifier=Modifier.padding(start=4.dp))

                Text(text= property.totalRent+"/m",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end=4.dp))
            }

            Row(
                modifier=Modifier.fillMaxWidth().padding(start=4.dp,end=4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ){
                Box(modifier=Modifier
                    .width(100.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))){
                    Text(text=property.selectedType,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }

                Box(modifier=Modifier
                    .width(100.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))){
                    Text(text=property.selectedBHK,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp,end=4.dp),
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text=property.location)
            }

            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .padding(start=4.dp,end=4.dp)) {

                items(property.selectedAmenities.size){it->
                    Item(property.selectedAmenities[it])
                }
            }
        }

    }

}

@Composable
fun Item(item: String){
    Box(
        modifier= Modifier
            .width(100.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
    ){
        Text(text=item)
    }
}