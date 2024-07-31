package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.microsoft.service.WorkingHoursFetcher
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.stream.Collectors

@Component
class DatesSuggesterService(
    private val workingHoursFetcher: WorkingHoursFetcher
) {
    fun suggestDaysForTheAuthenticatedUser(
        apiKey: String,
        client: OAuth2AuthorizedClient
    ): List<LocalDate> {
        val workingDays = workingHoursFetcher.findWorkingHoursFromAuthenticatedUser(client)!!.daysOfWeek

        val firstDayOfLastMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1)
        val lastDayOfLastMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())

        return firstDayOfLastMonth.datesUntil(lastDayOfLastMonth).filter { date ->
            workingDays!!.map { it.name.uppercase() }.contains(date.dayOfWeek.name)
        }.collect(Collectors.toList())
    }
}
