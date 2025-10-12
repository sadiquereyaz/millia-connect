package com.reyaz.feature.rent.domain.model

//currently i am creating this for testing purpose once it is done i will add other valures
//related to property like flat,location,bhk...
data class Property(
  val id:Int=0,
  val propertyTitle:String="",
  val propertyType:String="",
  val propertyBHK:String="",
  val propertyLocation:String="",
  val propertyRent:String="",
  val securityDeposit:String="",
  val propertyFloorNumber:String="",
  val totalFloor:String="",
  val propertyDescription:String="",
  val amenities:List<String> = emptyList()
)