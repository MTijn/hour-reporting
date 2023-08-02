package nl.martijnklene.hourreporting.model

import java.util.*

data class Category(
    val userId: UUID,
    val outlookTaskId: UUID?,
    val jiraProjectId: String,
    val default: Boolean
)
