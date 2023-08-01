package nl.martijnklene.hourreporting.model

import java.util.*

data class Category(
    val id: UUID,
    val userId: UUID,
    val outlookTaskId: UUID?,
    val jiraProjectId: String,
    val default: Boolean
)
