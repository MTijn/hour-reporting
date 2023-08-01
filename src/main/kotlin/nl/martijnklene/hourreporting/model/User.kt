package nl.martijnklene.hourreporting.model

import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val categories: List<Category>,
    val ignoredCategories: List<IgnoredCategory>,
    val apiKey: String,
    val jiraUserName: String
)
