package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.clockify.model.PostTimeEntry
import nl.martijnklene.hourreporting.clockify.service.ProjectsService
import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.controllers.dto.FormEntity
import nl.martijnklene.hourreporting.microsoft.service.WorkingHoursFetcher
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Component
class HoursPoster(
    private val projectsService: ProjectsService,
    private val timeEntriesService: TimeEntriesService,
    private val workingHoursFetcher: WorkingHoursFetcher
) {
    fun createTimeEntries(postedParams: FormEntity, apiKey: String, client: OAuth2AuthorizedClient) {
        if (postedParams.hours.isNullOrEmpty()) {
            throw RuntimeException("Missing hours to be posted")
        }

        val workingHours = workingHoursFetcher.findWorkingHoursFromAuthenticatedUser(client)
        postedParams.hours!!.forEach {
            var startTime = LocalDate.parse(it.key).atTime(6, 0).atZone(ZoneId.of("UTC"))
            if (workingHours != null) {
                startTime = LocalDate.parse(it.key).atTime(
                    workingHours.startTime!!.hour,
                    workingHours.startTime!!.minute
                ).atZone(ZoneId.of("Europe/Amsterdam")).withZoneSameInstant(ZoneId.of("UTC"))
            }

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
