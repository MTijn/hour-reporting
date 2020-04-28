package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.ZonedDateTime

data class PostTimeEntry(
    val billable: Boolean,
    val customFields: List<String>?,
    val end: ZonedDateTime,
    val projectId: String,
    val start: ZonedDateTime,
    val tagIds: List<String>?,
    val taskId: String
)
