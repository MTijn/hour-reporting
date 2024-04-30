package nl.martijnklene.hourreporting.tempo.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

data class WorkLog(
    val self: String,
    val tempoWorklogId: String,
    val issue: LinkedHashMap<String, String>,
    val timeSpentSeconds: Int,
    val billableSeconds: Int?,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val description: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val author: LinkedHashMap<String, Any>,
    val attributes: LinkedHashMap<String, Any>,
)
