package nl.martijnklene.hourreporting.jira.service

import kong.unirest.core.Unirest
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.jira.dto.Issue
import nl.martijnklene.hourreporting.jira.dto.JiraAccount
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue

@Component
class JiraUserFetcher(
    private val objectMapper: JsonMapper,
    private val encryption: StringEncryption
) {
    fun findUserDetails(
        userName: String,
        jiraToken: String
    ): JiraAccount? =
        objectMapper.readValue<JiraAccount>(
            Unirest
                .get("https://enreach-services.atlassian.net/rest/api/3/myself")
                .basicAuth(userName, jiraToken)
                .asJson()
                .body
                .toString()
        )

    fun findJiraIssuesByIssueKey(
        issueId: String,
        userName: String,
        jiraToken: String
    ): Issue =
        objectMapper.readValue<Issue>(
            Unirest
                .get("https://enreach-services.atlassian.net/rest/api/3/issue/$issueId")
                .basicAuth(userName, encryption.decryptText(jiraToken))
                .asJson()
                .body
                .toString()
        )
}
