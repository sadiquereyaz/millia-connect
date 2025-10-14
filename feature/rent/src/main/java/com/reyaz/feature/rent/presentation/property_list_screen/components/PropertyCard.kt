//package com.reyaz.feature.rent.presentation.property_list_screen.components
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.reyaz.feature.rent.domain.model.Property
//
//@Composable
//fun PropertyCard(property: Property, onDetailClick: (property: Property) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 8.dp, end = 8.dp, top = 32.dp)
//            .clickable {
//                onDetailClick(property)
//            },
//        shape = RoundedCornerShape(8.dp),
//        border = BorderStroke(1.dp, Color.LightGray),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.background
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 8.dp, end = 8.dp,top=10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = property.propertyTitle,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = property.propertyRent + "/m",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color =MaterialTheme.colorScheme.primary
//            )
//        }
//        Row(
//            modifier = Modifier
//                .padding(top=10.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//        ) {
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
//            ) {
//                Text(
//                    text = property.propertyType,
//                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
//                )
//            }
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
//            ) {
//                Text(
//                    text = property.propertyType,
//                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
//                )
//            }
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
//            ) {
//                Text(
//                    text = property.propertyType,
//                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
//                )
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 2.dp,top=10.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.LocationOn,
//                contentDescription = null
//            )
//            Text(text = property.propertyLocation)
//        }
//        LazyRow(modifier = Modifier
//            .fillMaxWidth(),
//            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            items(property.amenities.size){index->
//                val amenity=property.amenities[index]
//                AmenityItem(amenity)
//            }
//        }
//
//    }
//}
//
//@Composable
//fun AmenityItem(amenity:String,){
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
//    ) {
//        Text(
//            text = amenity,
//            modifier = Modifier.padding(start = 2.dp, end = 2.dp)
//        )
//    }
//}

@file:OptIn(ExperimentalLayoutApi::class)
package com.reyaz.feature.rent.presentation.property_list_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.feature.rent.domain.model.Property

@Composable
fun PropertyCard(property: Property, onDetailClick: (property: Property) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onDetailClick(property)
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title and Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = property.propertyTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${property.propertyRent}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "/month",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Property owner info
            Text(
                text = "by sdk",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Property Type, BHK, Floor - All in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PropertyDetailChip(text = property.propertyType )
                PropertyDetailChip(text = property.propertyBHK )
                PropertyDetailChip(text = property.propertyFloorNumber +"th floor" )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = property.propertyLocation,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities Row
            Row(
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                property.amenities.take(4).forEach { amenity ->
                    PropertyDetailChip(text = amenity)
                }
                if (property.amenities.size > 4) {
                    PropertyDetailChip(text = "+${property.amenities.size - 4}")
                }
            }
        }
    }
}

@Composable
fun PropertyDetailChip(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 13.sp,
            color = Color.DarkGray
        )
    }
}

