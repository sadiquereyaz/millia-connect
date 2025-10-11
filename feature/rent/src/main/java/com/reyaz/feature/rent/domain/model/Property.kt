package com.reyaz.feature.rent.domain.model

//currently i am creating this for testing purpose once it is done i will add other valures
//related to property like flat,location,bhk...
data class Property(
    val selectedBHK: String="",
    val selectedType:String="",
    val title:String="",
    val totalFloor: String="",
    val floorNumber:String="",
    val totalRent:String="",
    val securityDeposit: String="",
    val location: String="",
    val selectedAmenities:List<String> =emptyList()
)