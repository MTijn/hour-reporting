package nl.martijnklene.hourreporting.clockify.model

data class Task(
    val id: String,
    val name: String,
    val projectId: String,
    val assigneeIds: List<String>?,
    val assigneeId: String?,
    val estimate: String,
    val status: String
)
