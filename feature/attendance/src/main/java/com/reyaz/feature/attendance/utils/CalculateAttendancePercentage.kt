package com.reyaz.feature.attendance.utils

fun calculateAttendancePercentage(
    presentClasses: Int,
    totalClasses: Int
): Int {
    if (totalClasses == 0) return 100
    return ((presentClasses * 100f) / totalClasses).toInt()
}