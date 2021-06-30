package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.clockify.model.PostTimeEntry
import nl.martijnklene.hourreporting.clockify.service.ProjectsService
import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.controllers.dto.FormEntity
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Component
class HoursPoster(
    private val projectsService: ProjectsService,
    private val timeEntriesService: TimeEntriesService
) {
    fun createTimeEntries(postedParams: FormEntity, apiKey: String) {
        if (postedParams.hours.isNullOrEmpty()) {
            throw RuntimeException("Missing hours to be posted")
        }

        postedParams.hours!!.forEach {
            val startTime = LocalDate.parse(it.key).atTime(8, 0).atZone(ZoneId.of("UTC"))
            val hours = it.value["hours"]?.toLong()
            val duration = Duration.ofMinutes(hours!!)
            val endTime = ZonedDateTime.from(startTime).plus(duration ?: error("No Hours posted"))
            val project = projectsService.getProjectFromTaskId(it.value["taskId"] ?: error("Task ID not present"))
            val timeEntry = PostTimeEntry(
                true,
                emptyList(),
                endTime,
                project!!.id,
                startTime,
                emptyList(),
                it.value["taskId"] ?: error("Task ID not present")
            )
            timeEntriesService.postTimeEntry(timeEntry, apiKey)
        }
    }
}
