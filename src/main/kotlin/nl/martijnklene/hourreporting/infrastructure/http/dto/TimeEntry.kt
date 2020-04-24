package nl.martijnklene.hourreporting.infrastructure.http.dto

import java.time.LocalDate

data class TimeEntry(
    val taskId: String,
    val hours: Int,
    val date: LocalDate
)
