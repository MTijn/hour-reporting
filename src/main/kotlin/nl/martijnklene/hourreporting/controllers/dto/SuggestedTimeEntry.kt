package nl.martijnklene.hourreporting.controllers.dto

import java.time.Duration
import java.time.LocalDate

data class SuggestedTimeEntry(
    val duration: Duration,
    val taskId: String,
    val projectDescription: String,
    val date: LocalDate
)
