package nl.martijnklene.hourreporting.tempo.dto

import java.time.Instant

data class WorkLogRequest(
    val from: String,
    val to: String,
    val worker: Collection<String>
)
