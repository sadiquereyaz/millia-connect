package com.reyaz.feature.attendance.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * In Room, @Relation and @Embedded are the tools used to rebuild your normalized database tables into objects that your UI can actually use.
 * The @Embedded annotation tells Room to take all the columns from the lecture_slots table (id, subjectId, dayOfWeek, etc.) and flatten them into this class.
 *
 * [Relation]
 * Room doesn't use standard SQL JOIN statements for relations; instead, it performs two separate queries and handles the mapping for you automatically.
 * parentColumn = "subjectId": This refers to the column in the "Parent" (the @Embedded entity, which is LectureSlotEntity).
 * entityColumn = "subjectId": This refers to the column in the "Child" entity (SubjectEntity).
 * How Room executes this:
 * It first fetches the LectureSlotEntity.
 * It looks at the subjectId it just found (e.g., ID 101).
 * It then runs a second query: SELECT * FROM subjects WHERE subjectId = 101.
 * It assigns the result to the val subject property.
 *
 * USAGE:
 * @Transaction // Vital for @Relation!
 * @Query("SELECT * FROM lecture_slots WHERE dayOfWeek = :day")
 * fun getLecturesForDay(day: Int): Flow<List<LectureWithSubject>>
 */
data class LectureWithSubject(
    @Embedded   // flattens all columns of LectureSlotEntity into this object.
    val lecture: LectureSlotEntity,

    @Relation(parentColumn = "subjectId", entityColumn = "subjectId")   // defines a 1-to-1 relationship. ie, Take lecture.subjectId -> Find a row in subjects Where subjects.subjectId == lecture.subjectId, Map it into SubjectEntity
    val subject: SubjectEntity
)

// When I query lectures, also fetch the related subject automatically.