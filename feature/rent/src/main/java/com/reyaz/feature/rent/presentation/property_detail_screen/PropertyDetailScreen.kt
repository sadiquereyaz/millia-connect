package com.reyaz.feature.rent.presentation.property_detail_screen

import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.reyaz.feature.rent.domain.model.Property


@Composable
fun PropertyDetailScreen(property: Property) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            if (property.urlList.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(property.urlList.size) { index ->
                        Image(
                            painter = rememberAsyncImagePainter(property.urlList[index]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillParentMaxWidth(0.9f)
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No images available")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = property.propertyTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = property.propertyLocation,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Rent: ₹${property.propertyRent} / month",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Security Deposit: ₹${property.securityDeposit}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "${property.propertyType} • ${property.propertyBHK}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Floor: ${property.propertyFloorNumber} / ${property.totalFloor}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = property.propertyDescription,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Amenities ---
            Text(
                text = "Amenities",
                style = MaterialTheme.typography.titleMedium
            )
            Column(verticalArrangement = Arrangement.spacedBy(400.dp)) {
                property.amenities.forEach { amenity ->
                    Text("• $amenity", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(ButtonDefaults.MinHeight + 16.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.BottomCenter)
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Share, contentDescription = "Share",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "Share")
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF60F279),
                    contentColor = Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Whatsapp,
                    contentDescription = "Share",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "Contact")
            }
        }
    }
}
