package nl.martijnklene.hourreporting.model

import java.util.*

data class User(
    val id: UUID,
    val name: String,
    var categories: List<Category>,
    var ignoredCategories: List<IgnoredCategory>,
    val jiraApiKey: String,
    val tempoApiKey: String,
    val jiraUserName: String,
    val jiraAccountId: String,
    val photo: String? = null
)
