package com.reyaz.feature.rent.domain.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
  val id: String = "1",
  val ownerName: String? = "Sadique Reyaz Android Intern",
  val ownerPicUrl: String? = "https://avatars.githubusercontent.com/u/118601913?s=400&u=752ca858776d252fabc6126797f6aaa3f5e9912a&v=4",
  val urlList: List<String> = emptyList(),
  val propertyTitle: String = "Spacious 2BHK Apartment. Immediate move in only! Comes with all modern amenities.",
  val propertyType: String = "Apartment",
  val propertyBHK: String = "2 BHK",  // todo: remove
  val propertySizeCount: Int? = 3,
  val propertyLocation: String = "Koramangala, Bangalore",
  val propertyRent: String = "25000",
  val propertyFloorNumber: String = "3",
  val totalFloor: String = "5",
  val propertyDescription: String = "A beautiful and spacious apartment located in the heart of the city. Comes with all modern amenities.",
  val amenities: List<String> = listOf("Lift", "Security", "Parking", "Power Backup"),
  val securityAmount: Int? = 10000,
  val brokerageAmount: Int? = 5000,
  @Deprecated("use securityAmount") val securityDeposit: String = "50000",    // todo: remove
  val gender: Gender = Gender.MALE,
  val requiredHeadCount: Int = 5,
  val furnishType : FurnishType? = FurnishType.NOT_FURNISHED,
  val postDate: Long? = System.currentTimeMillis(),
  val reportCount: Int = 0,
) : Parcelable {
}

enum class Gender{
  MALE, FEMALE
}

data class GenderChip(
  val text: String,
  val icon: ImageVector,
  val lightIconColor: Color,
  val darkIconColor: Color
)

enum class FurnishType(val value: String) {
  NOT_FURNISHED("Not-Furnished"),
  SEMI_FURNISHED("Semi-Furnished"),
  FULLY_FURNISHED("Furnished")
}