package nl.martijnklene.hourreporting.infrastructure.dto

import java.time.Duration
import java.time.LocalDate

data class SuggestedTimeEntry(
    val duration: Duration,
    val projectId: String,
    val projectDescription: String,
    val date: LocalDate
)
