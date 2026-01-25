package com.reyaz.feature.attendance.domain.usecase

import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import com.reyaz.feature.attendance.domain.repo.AttendanceRepository
import com.reyaz.feature.attendance.presentation.graph.model.DonutChartItem
import com.reyaz.feature.attendance.presentation.graph.model.GraphData
import com.reyaz.feature.attendance.presentation.graph.model.LineData
import java.time.LocalDate
import java.time.YearMonth

class GetAttendanceGraphUseCase(
    private val repository: AttendanceRepository
) {
    suspend operator fun invoke(): Pair<GraphData, List<DonutChartItem>> {

        val records = repository.getAttendanceRecord()

        val graphData = records.toGraphData()
        val donutData = records.toDonutChartData()

        return graphData to donutData
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

private fun List<AttendanceRecord>.toDonutChartData(): List<DonutChartItem> {

    if (isEmpty()) return emptyList()

    return this
        .groupBy { it.subjectName }
        .map { (subject, records) ->

            val presentCount = records.count { it.isPresent }
            val totalCount = records.size

            DonutChartItem(
                label = subject,
                presentCount = presentCount,
                totalCount = totalCount
            )
        }
}
