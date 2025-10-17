@file:OptIn(ExperimentalLayoutApi::class)

package com.reyaz.feature.rent.presentation.property_list_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reyaz.feature.rent.domain.model.Gender
import com.reyaz.feature.rent.domain.model.GenderChip
import com.reyaz.feature.rent.domain.model.Property

@Composable
fun PropertyCard(property: Property, onDetailClick: (property: Property) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDetailClick(property)
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                property.urlList.getOrNull(0)?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(height = 120.dp, width = 100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        clipToBounds = true
                    )
                    Spacer(Modifier.width(12.dp))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // title
                    Text(
                        text = property.propertyTitle + property.propertyTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Title, Price and author Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "₹${property.propertyRent}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "/month",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        property.securityAmount?.let {
                            Text(
                                text = "+",
                                maxLines = 1,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "₹${it} Security",
                                maxLines = 1,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                    }
                    // Location Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = property.propertyLocation,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val genderData = if (property.gender == Gender.MALE) GenderChip(
                        text = "Male",
                        icon = Icons.Default.Male,
                        lightIconColor = Color(0xFF394DBD),
                        darkIconColor = Color(0xFF2196F3)
                    ) else GenderChip(
                        text = "Female",
                        icon = Icons.Default.Female,
                        lightIconColor = Color(0xFFE258A6),
                        darkIconColor = Color(0xFFFFABDE)
                    )
                    PropertyDetailChip(
                        iconDarkColor = genderData.darkIconColor,
                        iconLightColor = genderData.lightIconColor,
                        icon = genderData.icon,
                        text = "For ${property.requiredHeadCount} " + genderData.text
                    )

                }
                property.propertyCount?.let {
                    item {
                        PropertyDetailChip(
                            iconDarkColor = Color(0xFF87B056),
                            iconLightColor = Color(0xFF4C7522),
                            icon = Icons.Default.MapsHomeWork,
                            text = "$it BHK"
                        )
                    }
                }
                property.furnishType?.let {
                    item {
                        PropertyDetailChip(
                            iconDarkColor = Color(0xFFC98A44),
                            iconLightColor = Color(0xFF976733),
                            icon = Icons.Default.Bed,
                            text = it.value
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                property.ownerPicUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(8.dp))
                }
                property.ownerName?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(3f)
                    )
                }
                property.postedDateText?.let {
                    Spacer(Modifier.weight(1f))
                    Text(
                        it,
                        maxLines = 1,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun PropertyDetailChip(
    iconDarkColor: Color,
    iconLightColor: Color,
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(100))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = if (isSystemInDarkTheme()) iconDarkColor else iconLightColor,
            contentDescription = null
        )

        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
