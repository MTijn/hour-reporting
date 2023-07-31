package nl.martijnklene.hourreporting.tempo.dto

import java.time.LocalDate

data class WorkLog(
    val billableSeconds: Int,
    val comment: String,
    val originTaskIs: String,
    val worker: String,
    val started: LocalDate
)
