package nl.martijnklene.hourreporting.tempo.model

data class Issue(
    val issueType: String,
    val projectId: Int,
    val projectKey: String,
    val iconUrl: String,
    val summary: String,
    val reporterKey: String,
    val internalIssue: Boolean,
    val versions: Collection<String>,
    val epicKey: String?,
    val epicIssue: Epic?,
    val accountKey: String?,
    val issueStatus: String,
    val components: Collection<String>,
    val key: String,
    val id: Int
)
