package com.reyaz.feature.attendance.domain.model

import com.reyaz.core.common.utils.extensions.StringUtils.toCapSmall


enum class AddFieldDialogType(
    val label: String
){
    SUBJECT(
        label = "Engineering Mathematics"
    ),
    LOCATION(
        label = "FET Building"
    );

    val displayName: String
        get() = name.toCapSmall()
}