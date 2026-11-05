package nl.martijnklene.hourreporting.tempo.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tools.jackson.module.kotlin.jacksonMapperBuilder
import java.time.ZoneId
import java.time.ZonedDateTime

class WorkLogTest {
    private val objectMapper = jacksonMapperBuilder().build()

    @Test
    fun `serializes the start in the format jira expects`() {
        val workLog =
            WorkLog(
                timeSpentSeconds = 3600,
                comment = "Coding",
                started = ZonedDateTime.of(2026, 10, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
            )

        val json = objectMapper.readTree(objectMapper.writeValueAsString(workLog))

        assertEquals(3600, json["timeSpentSeconds"].asInt())
        assertEquals("Coding", json["comment"].asString())
        assertEquals("2026-10-01T02:00:00.000+0200", json["started"].asString())
    }
}
