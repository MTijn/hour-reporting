package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.LocalDateTime

data class PostTimeEntry(
    val billable: Boolean,
    val customFields: List<String>?,
    val end: LocalDateTime,
    val projectId: String,
    val start: LocalDateTime,
    val tagIds: List<String>?,
    val taskId: String
)
