package nl.martijnklene.hourreporting.service

import com.microsoft.graph.models.DateTimeTimeZone
import com.microsoft.graph.models.Event
import com.microsoft.graph.models.ResponseStatus
import com.microsoft.graph.models.ResponseType
import nl.martijnklene.hourreporting.controllers.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.microsoft.service.CalendarEventsFetcher
import nl.martijnklene.hourreporting.model.IgnoredCategory
import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aCategory
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.support.anAuthorizedClient
import nl.martijnklene.hourreporting.tempo.model.WorkLog
import nl.martijnklene.hourreporting.tempo.model.WorkLogs
import nl.martijnklene.hourreporting.tempo.service.WorkLogFetcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

class HoursSuggestionCalculatorTest {
    private val calendarEventsFetcher = mock(CalendarEventsFetcher::class.java)
    private val datesSuggesterService = mock(DatesSuggesterService::class.java)
    private val workLogFetcher = mock(WorkLogFetcher::class.java)

    private val calculator =
        HoursSuggestionCalculator(
            calendarEventsFetcher,
            CategoryMapper(),
            IgnoredCategories(),
            datesSuggesterService,
            workLogFetcher
        )

    private val date = LocalDate.of(2026, 10, 1)
    private val client = anAuthorizedClient()
    private val user =
        aUser(
            categories =
                listOf(
                    aCategory("Development", "DEV-1", 100),
                    aCategory("General", "GEN-1", 300, default = true)
                ),
            ignoredCategories = listOf(IgnoredCategory(USER_ID, "Lunch"))
        )

    @BeforeEach
    fun setUp() {
        givenLoggedSeconds(date)
    }

    @Test
    fun `suggests a full day on the default category when there are no events`() {
        givenEvents(date)

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(8), 300, "GEN-1", "Meeting", date)), suggestions)
    }

    @Test
    fun `suggests nothing when the day is already fully logged`() {
        givenLoggedSeconds(date, 4 * 3600, 4 * 3600)
        givenEvents(date, anEvent("Standup", "09:00", "09:15", "Development"))

        assertEquals(emptyList<SuggestedTimeEntry>(), calculator.suggestHoursForADay(date, client, user))
    }

    @Test
    fun `subtracts already logged time from the remaining suggestion`() {
        givenLoggedSeconds(date, 3 * 3600)
        givenEvents(date)

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(5), 300, "GEN-1", "Meeting", date)), suggestions)
    }

    @Test
    fun `suggests accepted meetings and fills the rest of the day with the default category`() {
        givenEvents(
            date,
            anEvent("Refinement", "09:00", "10:30", "Development"),
            anEvent("Organised sync", "11:00", "11:30", null, ResponseType.Organizer)
        )

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(
            listOf(
                SuggestedTimeEntry(Duration.ofMinutes(90), 100, "DEV-1", "Refinement", date),
                SuggestedTimeEntry(Duration.ofMinutes(30), 300, "GEN-1", "Organised sync", date),
                SuggestedTimeEntry(Duration.ofMinutes(360), 300, "GEN-1", "Meeting", date)
            ),
            suggestions
        )
    }

    @Test
    fun `skips meetings that were not accepted`() {
        givenEvents(
            date,
            anEvent("Declined", "09:00", "10:00", "Development", ResponseType.Declined),
            anEvent("Tentative", "10:00", "11:00", "Development", ResponseType.TentativelyAccepted),
            anEvent("No answer", "11:00", "12:00", "Development", ResponseType.NotResponded)
        )

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(8), 300, "GEN-1", "Meeting", date)), suggestions)
    }

    @Test
    fun `skips meetings in an ignored category without deducting their time`() {
        givenEvents(date, anEvent("Lunch", "12:00", "12:30", "Lunch"))

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(8), 300, "GEN-1", "Meeting", date)), suggestions)
    }

    @Test
    fun `caps a meeting at the time that is left to log`() {
        givenLoggedSeconds(date, 7 * 3600)
        givenEvents(date, anEvent("Workshop", "09:00", "12:00", "Development"))

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(1), 100, "DEV-1", "Workshop", date)), suggestions)
    }

    @Test
    fun `an all day event claims the remaining time of the day`() {
        givenEvents(
            date,
            anEvent("Conference", "00:00", "00:00", "Development", allDay = true),
            anEvent("Later meeting", "14:00", "15:00", "Development")
        )

        val suggestions = calculator.suggestHoursForADay(date, client, user)

        assertEquals(listOf(SuggestedTimeEntry(Duration.ofHours(8), 100, "DEV-1", "Conference", date)), suggestions)
    }

    @Test
    fun `uses zero and an empty key when no category matches and there is no default`() {
        val userWithoutCategories = aUser()
        givenLoggedSeconds(date, forUser = userWithoutCategories)
        givenEvents(date, anEvent("Unmapped", "09:00", "10:00", "Something"))

        val suggestions = calculator.suggestHoursForADay(date, client, userWithoutCategories)

        assertEquals(
            listOf(
                SuggestedTimeEntry(Duration.ofHours(1), 0, "", "Unmapped", date),
                SuggestedTimeEntry(Duration.ofHours(7), 0, "", "Meeting", date)
            ),
            suggestions
        )
    }

    @Test
    fun `combines the suggestions of all suggested days`() {
        val secondDate = date.plusDays(1)
        `when`(datesSuggesterService.suggestDaysForTheAuthenticatedUser(user.jiraApiKey, client))
            .thenReturn(listOf(date, secondDate))
        givenLoggedSeconds(secondDate, 2 * 3600)
        givenEvents(date)
        givenEvents(secondDate)

        val suggestions = calculator.suggestHoursForAnAuthenticatedUser(user, client)

        assertEquals(
            listOf(
                SuggestedTimeEntry(Duration.ofHours(8), 300, "GEN-1", "Meeting", date),
                SuggestedTimeEntry(Duration.ofHours(6), 300, "GEN-1", "Meeting", secondDate)
            ),
            suggestions
        )
    }

    private fun givenEvents(
        day: LocalDate,
        vararg events: Event
    ) {
        `when`(calendarEventsFetcher.getEventsForADay(day, client)).thenReturn(events.toList())
    }

    private fun givenLoggedSeconds(
        day: LocalDate,
        vararg seconds: Int,
        forUser: User = user
    ) {
        `when`(workLogFetcher.fetchWorkLogsBetweenDates(day, day, forUser))
            .thenReturn(WorkLogs("", LinkedHashMap(), seconds.map { aWorkLog(day, it) }))
    }

    private fun aWorkLog(
        day: LocalDate,
        seconds: Int
    ) = WorkLog(
        self = "",
        tempoWorklogId = "1",
        issue = LinkedHashMap(),
        timeSpentSeconds = seconds,
        billableSeconds = seconds,
        startDate = day,
        startTime = LocalTime.of(9, 0),
        description = "",
        createdAt = ZonedDateTime.now(),
        updatedAt = ZonedDateTime.now(),
        author = LinkedHashMap(),
        attributes = LinkedHashMap()
    )

    private fun anEvent(
        subject: String,
        start: String,
        end: String,
        category: String?,
        response: ResponseType = ResponseType.Accepted,
        allDay: Boolean = false
    ): Event {
        val event = Event()
        event.subject = subject
        event.isAllDay = allDay
        event.categories = listOfNotNull(category)
        event.responseStatus = ResponseStatus().also { it.response = response }
        event.start = DateTimeTimeZone().also { it.dateTime = "${date}T$start:00.0000000" }
        event.end = DateTimeTimeZone().also { it.dateTime = "${date}T$end:00.0000000" }
        return event
    }
}
