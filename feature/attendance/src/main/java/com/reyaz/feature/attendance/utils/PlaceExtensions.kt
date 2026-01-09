package com.reyaz.feature.attendance.utils

import com.mappls.sdk.services.api.Place

/**
 * Extension function to convert Place object to a detailed string representation
 * for logging purposes.
 */
fun Place.toDetailedString(): String {
    return """
        Place Details:
        - houseNumber: $houseNumber;
        - houseName: $houseName;
        - poi: $poi;
        - poiDist: $poiDist;
        - street: $street;
        - streetDist: $streetDist;
        - subSubLocality: $subSubLocality;
        - subLocality: $subLocality;
        - locality: $locality;
        - village: $village;
        - district: $district;
        - subDistrict: $subDistrict;
        - city: $city;
        - state: $state;
        - pincode: $pincode;
        - lat: $lat;
        - lng: $lng;
        - area: $area;
        - formattedAddress: $formattedAddress;
        - mapplsPin: $mapplsPin;
        - areaCode: $areaCode;
        - twnName: $twnName;
        - vlgCenCd: $vlgCenCd;
        - vlgLgdCd: $vlgLgdCd;
        - sdbCenCd: $sdbCenCd;
        - sdbLgdCd: $sdbLgdCd;
        - dstCenCd: $dstCenCd;
        - dstLgdCd: $dstLgdCd;
        - sttCenCd: $sttCenCd;
        - sttLgdCd: $sttLgdCd;
        - twnCenCd: $twnCenCd;
        - twnLgdCd: $twnLgdCd;
        - isRoofTop: $isRoofTop;
        - richInfo: $richInfo;
        - EntryCoordinate: $entryCoordinates;
    """.trimIndent()
}

/**
 * Extension function to get a short address representation from Place object.
 * Returns the most relevant address component available.
 */
fun Place.getShortAddress(): String {
    return formattedAddress
        ?: locality
        ?: area
        ?: city
        ?: district
        ?: village
        ?: twnName
        ?: "Unknown Location"
}
