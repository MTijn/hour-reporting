package nl.martijnklene.hourreporting.jira.dto

data class Issue(
    val expand: String,
    val id: Int,
    val self: String,
    val key: String,
    val fields: LinkedHashMap<String, Any>,
)
