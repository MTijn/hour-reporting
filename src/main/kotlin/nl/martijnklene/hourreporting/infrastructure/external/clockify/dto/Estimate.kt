package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.Duration

data class Estimate(
    val estimate: Duration,
    val type: String
)
