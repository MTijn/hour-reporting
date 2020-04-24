package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

data class TimeEntry(
    val billable: Boolean,
    val description: String,
    val id: String?,
    val isLocked: Boolean,
    val projectId: String,
    val tagIds: List<String>?,
    val taskId: String?,
    val timeInterval: TimeInterval,
    val userId: String,
    val workspaceId: String
)
