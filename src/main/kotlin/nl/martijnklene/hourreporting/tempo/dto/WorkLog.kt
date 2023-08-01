package nl.martijnklene.hourreporting.tempo.dto

import java.time.LocalDate

data class WorkLog(
    val attributes: Map<String, Any>? = emptyMap(),
    val billableSeconds: Int? = null,
    val comment: String? = null,
    val endDate: LocalDate? = null,
    val includeNonWorkingDays: Boolean = false,
    val originTaskId: String,
    val remainingEstimate: String? = null,
    val started: LocalDate,
    val timeSpentSeconds: Int,
    val worker: String
)
