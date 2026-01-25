package com.reyaz.feature.attendance.utils

import kotlin.math.ceil

fun generateAttendanceWarningMessage(
    totalClassesHeld: Int,
    classesAttended: Int,
    targetPercentage: Int
): String {

    if (totalClassesHeld == 0) {
        return "No classes held yet. You are safe."
    }

    val currentPercentage =
        (classesAttended * 100f) / totalClassesHeld

    // Case 1: already above target
    if (currentPercentage >= targetPercentage) {
        val maxMissable =
            ((classesAttended * 100f) / targetPercentage - totalClassesHeld)
                .toInt()

        return if (maxMissable > 0)
                "You can miss next $maxMissable class(es)."
            else
                "Try not to miss the next class to stay above $targetPercentage%."
    }

    // Case 2: below target → must attend classes
    val numerator = targetPercentage * totalClassesHeld - 100 * classesAttended
    val denominator = 100 - targetPercentage

    val classesNeeded =
        ceil(numerator.toDouble() / denominator).toInt()

    return "Should not miss next $classesNeeded class to reach $targetPercentage%."
}