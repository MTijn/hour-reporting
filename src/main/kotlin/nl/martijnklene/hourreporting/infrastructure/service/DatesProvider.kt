package nl.martijnklene.hourreporting.infrastructure.service

import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
import nl.martijnklene.hourreporting.infrastructure.repository.WorkingHoursHttpRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.stream.*

@Component
class DatesProvider(
    private val workingHoursRepository: WorkingHoursHttpRepository,
    private val timeEntries: TimeEntries
) {
    fun suggestDaysForTheAuthenticatedUser(authentication: Authentication): List<LocalDate> {
        val workingHours = workingHoursRepository.findWorkingHoursFromAuthentication(authentication)
        val workingDays = workingHours.daysOfWeek

        val lastTimeEntry = timeEntries.lastClockifyTimeEntry()
        val lastDate = lastTimeEntry!!.timeInterval.start.toLocalDate().plusDays(1)
        val today = LocalDate.now()

        return lastDate.datesUntil(today).filter { date -> workingDays.map { it.name }.contains(date.dayOfWeek.name)}.collect(Collectors.toList())
    }
}
