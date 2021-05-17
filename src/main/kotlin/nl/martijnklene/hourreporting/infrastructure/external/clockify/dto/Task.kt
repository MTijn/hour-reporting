package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

data class Task(
    val id: String,
    val name: String,
    val projectId: String,
    val assigneeIds: List<String>?,
    val assigneeId: String?,
    val estimate: String,
    val status: String
)
