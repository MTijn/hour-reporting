package nl.martijnklene.hourreporting.clockify.model

import java.time.Duration

data class Estimate(
    val estimate: Duration,
    val type: String
)
