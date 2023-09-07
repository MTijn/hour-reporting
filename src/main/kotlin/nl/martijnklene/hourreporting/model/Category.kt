package nl.martijnklene.hourreporting.model

import java.util.*

data class Category(
    val userId: UUID,
    val categoryName: String,
    val jiraProjectId: String,
    val default: Boolean
)
