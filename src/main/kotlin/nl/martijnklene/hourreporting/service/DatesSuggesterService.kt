package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.microsoft.service.WorkingHoursFetcher
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.stream.Collectors

@Component
class DatesSuggesterService(
    private val timeEntriesService: TimeEntriesService,
    private val workingHoursFetcher: WorkingHoursFetcher
) {
    fun suggestDaysForTheAuthenticatedUser(apiKey: String): List<LocalDate> {
        val workingDays = workingHoursFetcher.findWorkingHoursFromAuthenticatedUser()!!.daysOfWeek

        val lastTimeEntry = timeEntriesService.lastClockifyTimeEntry(apiKey)
        val lastDate = lastTimeEntry!!.timeInterval.start.toLocalDate().plusDays(1)
        val today = LocalDate.now()

        return lastDate.datesUntil(today).filter { date ->
            workingDays!!.map { it.name }
                .contains(date.dayOfWeek.name)
        }
            .collect(Collectors.toList())
    }
}
