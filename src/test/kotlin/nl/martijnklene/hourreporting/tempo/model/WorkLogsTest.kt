package nl.martijnklene.hourreporting.tempo.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tools.jackson.module.kotlin.jacksonMapperBuilder
import tools.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.LocalTime

class WorkLogsTest {
    private val objectMapper = jacksonMapperBuilder().build()

    @Test
    fun `reads a tempo work log search response`() {
        val json =
            """
            {
              "self": "https://api.eu.tempo.io/4/worklogs/search",
              "metadata": { "count": 1, "offset": 0, "limit": 500 },
              "results": [
                {
                  "self": "https://api.eu.tempo.io/4/worklogs/42",
                  "tempoWorklogId": "42",
                  "issue": { "self": "https://example.atlassian.net/rest/api/2/issue/100", "id": "100" },
                  "timeSpentSeconds": 5400,
                  "billableSeconds": null,
                  "startDate": "2026-10-01",
                  "startTime": "09:30:00",
                  "description": "Refinement",
                  "createdAt": "2026-10-01T10:00:00Z",
                  "updatedAt": "2026-10-01T10:00:00Z",
                  "author": { "accountId": "jira-account-id" },
                  "attributes": { "values": [] }
                }
              ]
            }
            """.trimIndent()

        val workLogs = objectMapper.readValue<WorkLogs>(json)

        assertEquals(1, workLogs.metadata["count"])
        val workLog = workLogs.results.single()
        assertEquals("42", workLog.tempoWorklogId)
        assertEquals(5400, workLog.timeSpentSeconds)
        assertEquals(null, workLog.billableSeconds)
        assertEquals(LocalDate.of(2026, 10, 1), workLog.startDate)
        assertEquals(LocalTime.of(9, 30), workLog.startTime)
        assertEquals("Refinement", workLog.description)
    }
}
