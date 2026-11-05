package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.controllers.response.PostedHours
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.tempo.dto.WorkLog
import nl.martijnklene.hourreporting.tempo.service.WorkLogPoster
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import java.time.ZoneId
import java.time.ZonedDateTime

class HoursPosterTest {
    private val workLogPoster = mock(WorkLogPoster::class.java)
    private val hoursPoster = HoursPoster(workLogPoster)
    private val user = aUser()

    @Test
    fun `posts a work log for every submitted hour entry`() {
        val postedHours =
            PostedHours(
                hashMapOf(
                    "2026-10-01" to
                        hashMapOf(
                            "hours" to "60,90",
                            "taskId" to "DEV-1,SUP-2",
                            "projectDescription" to "Coding,Helping"
                        )
                )
            )

        hoursPoster.createTimeEntries(postedHours, user)

        val expectedStart = ZonedDateTime.of(2026, 10, 1, 2, 0, 0, 0, ZoneId.of("Europe/Amsterdam"))
        verify(workLogPoster).postWorkLogItem(
            "encrypted-jira-key",
            "DEV-1",
            WorkLog(timeSpentSeconds = 3600, comment = "Coding", started = expectedStart)
        )
        verify(workLogPoster).postWorkLogItem(
            "encrypted-jira-key",
            "SUP-2",
            WorkLog(timeSpentSeconds = 5400, comment = "Helping", started = expectedStart)
        )
    }

    @Test
    fun `throws when no hours are posted`() {
        val exception =
            assertThrows<RuntimeException> {
                hoursPoster.createTimeEntries(PostedHours(null), user)
            }

        assertEquals("Missing hours to be posted", exception.message)
        verifyNoInteractions(workLogPoster)
    }

    @Test
    fun `throws when an empty set of hours is posted`() {
        assertThrows<RuntimeException> {
            hoursPoster.createTimeEntries(PostedHours(hashMapOf()), user)
        }
        verifyNoInteractions(workLogPoster)
    }
}
