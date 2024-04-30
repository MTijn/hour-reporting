package nl.martijnklene.hourreporting.jira.dto

data class JiraAccount(
    val accountId: String,
    val accountType: String,
    val active: Boolean,
    val applicationRoles: LinkedHashMap<String, Any>,
    val avatarUrls: LinkedHashMap<String, Any>,
    val displayName: String,
    val emailAddress: String,
    val groups: LinkedHashMap<String, Any>,
    val self: String,
    val locale: String,
    val timeZone: String,
    val expand: String,
)
