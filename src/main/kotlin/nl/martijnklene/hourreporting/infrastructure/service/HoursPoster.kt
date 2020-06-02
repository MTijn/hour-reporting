package nl.martijnklene.hourreporting.infrastructure.service

import nl.martijnklene.hourreporting.infrastructure.exception.NoHoursPosted
import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.PostTimeEntry
import nl.martijnklene.hourreporting.infrastructure.http.dto.FormEntity
import nl.martijnklene.hourreporting.infrastructure.repository.ProjectHttpRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Component
class HoursPoster(
    private val projectHttpRepository: ProjectHttpRepository,
    private val timeEntries: TimeEntries
) {
    fun createTimeEntries(postedParams: FormEntity) {
        if (postedParams.hours.isNullOrEmpty()) {
            throw NoHoursPosted("Missing hours to be posted")
        }

        postedParams.hours!!.forEach {
            val startTime = LocalDate.parse(it.key).atTime(8, 0).atZone(ZoneId.of("Europe/Amsterdam"))
            val endTime = ZonedDateTime.from(startTime).plusHours(it.value["hours"]?.toLong() ?: error("No Hours posted"))
            val project = projectHttpRepository.getProjectFromTaskId(it.value["taskId"] ?: error("Task ID not present"))
            val timeEntry = PostTimeEntry(
                true,
                emptyList(),
                endTime,
                project!!.id,
                startTime,
                emptyList(),
                it.value["taskId"] ?: error("Task ID not present")
            )
            timeEntries.postTimeEntry(timeEntry)
        }
    }
}
