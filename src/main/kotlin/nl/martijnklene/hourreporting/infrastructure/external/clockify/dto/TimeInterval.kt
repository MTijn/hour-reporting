package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.Duration
import java.time.LocalDateTime

data class TimeInterval(
    val duration: Duration,
    val start: LocalDateTime,
    val end: LocalDateTime
)
