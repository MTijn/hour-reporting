package nl.martijnklene.hourreporting.tempo.dto

data class WorkLogRequest(
    val from: String,
    val to: String,
    val authorIds: Collection<String>
)
