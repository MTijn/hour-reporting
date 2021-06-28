package nl.martijnklene.hourreporting.clockify.model

import java.time.Duration
import java.time.ZonedDateTime

data class TimeInterval(
    val duration: Duration,
    val start: ZonedDateTime,
    val end: ZonedDateTime
)
