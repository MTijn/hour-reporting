package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.Duration
import java.time.ZonedDateTime

data class TimeInterval(
    val duration: Duration,
    val start: ZonedDateTime,
    val end: ZonedDateTime
)
