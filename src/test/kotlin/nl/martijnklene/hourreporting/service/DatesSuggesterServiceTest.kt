package nl.martijnklene.hourreporting.service

import com.microsoft.graph.models.DayOfWeek
import com.microsoft.graph.models.WorkingHours
import nl.martijnklene.hourreporting.microsoft.service.WorkingHoursFetcher
import nl.martijnklene.hourreporting.support.anAuthorizedClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class DatesSuggesterServiceTest {
    private val workingHoursFetcher = mock(WorkingHoursFetcher::class.java)
    private val service = DatesSuggesterService(workingHoursFetcher)
    private val client = anAuthorizedClient()

    @Test
    fun `suggests every working day of the previous month`() {
        givenWorkingDays(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Thursday, DayOfWeek.Friday)
        val firstDayOfLastMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1)
        val expected =
            firstDayOfLastMonth
                .datesUntil(firstDayOfLastMonth.plusMonths(1))
                .filter { it.dayOfWeek !in listOf(java.time.DayOfWeek.SATURDAY, java.time.DayOfWeek.SUNDAY) }
                .toList()

        val suggestedDays = service.suggestDaysForTheAuthenticatedUser("api-key", client)

        assertEquals(expected, suggestedDays)
    }

    @Test
    fun `only suggests the days the user works`() {
        givenWorkingDays(DayOfWeek.Monday, DayOfWeek.Wednesday)

        val suggestedDays = service.suggestDaysForTheAuthenticatedUser("api-key", client)

        assertTrue(suggestedDays.isNotEmpty())
        assertTrue(
            suggestedDays.all {
                it.dayOfWeek == java.time.DayOfWeek.MONDAY || it.dayOfWeek == java.time.DayOfWeek.WEDNESDAY
            }
        )
    }

    @Test
    fun `suggests nothing when the user has no working days`() {
        givenWorkingDays()

        assertEquals(emptyList<LocalDate>(), service.suggestDaysForTheAuthenticatedUser("api-key", client))
    }

    private fun givenWorkingDays(vararg days: DayOfWeek) {
        val workingHours = WorkingHours()
        workingHours.daysOfWeek = days.toList()
        `when`(workingHoursFetcher.findWorkingHoursFromAuthenticatedUser(client)).thenReturn(workingHours)
    }
}
