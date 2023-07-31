package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.microsoft.service.WorkingHoursFetcher
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DatesSuggesterService(
    private val workingHoursFetcher: WorkingHoursFetcher
) {
    fun suggestDaysForTheAuthenticatedUser(apiKey: String, client: OAuth2AuthorizedClient): List<LocalDate> {
        val workingDays = workingHoursFetcher.findWorkingHoursFromAuthenticatedUser(client)!!.daysOfWeek

        val today = LocalDate.now()
        return listOf(today)
    }
}
