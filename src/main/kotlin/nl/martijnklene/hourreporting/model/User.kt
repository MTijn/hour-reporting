package nl.martijnklene.hourreporting.model

import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val categories: List<Category>,
    val ignoredCategories: List<IgnoredCategory>,
    val jiraApiKey: String,
    val tempoApiKey: String,
    val jiraUserName: String,
    val jiraAccountId: String,
    val photo: String? = null
)
