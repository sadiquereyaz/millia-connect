package com.reyaz.feature.attendance.domain.usecase

import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.presentation.graph.model.GraphData
import com.reyaz.feature.attendance.presentation.graph.model.LineData
import java.time.LocalDate
import java.time.YearMonth

class GetAttendanceGraphUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): GraphData {

        val records = repository.getAttendanceRecord()

        return records.toGraphData()
    }
}

fun List<AttendanceRecord>.toGraphData(): GraphData {

    val subjects = this.map { it.subjectName }.distinct()

    val groupedByMonth = this.groupBy {
        YearMonth.from(
            LocalDate.ofEpochDay(it.date.toLong())
        )
    }

    val lineData = groupedByMonth.map { (month, records) ->

        val percentages = subjects.map { subject ->
            val subjectRecords = records.filter { it.subjectName == subject }

            if (subjectRecords.isEmpty()) 0f
            else {
                val present = subjectRecords.count { it.isPresent }
                (present * 100f) / subjectRecords.size
            }
        }

        LineData(
            month = month.month,
            percentages = percentages
        )
    }

    return GraphData(
        subjects = subjects,
        lineData = lineData
    )
}
