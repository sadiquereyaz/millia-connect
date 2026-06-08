package com.reyaz.core.common.utils

fun Boolean?.orFalse(): Boolean{
    return this ?: false
}

fun Boolean?.orTrue(): Boolean{
    return this ?: true
}

