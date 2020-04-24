package nl.martijnklene.hourreporting.infrastructure.http.rest

import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.PostTimeEntry
import nl.martijnklene.hourreporting.infrastructure.http.dto.TimeEntry
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/time_entries")
class TimeEntries(private val timeEntries: TimeEntries) {

    @PostMapping("/")
    fun addTimeEntry(@Valid @RequestBody timeEntry: TimeEntry) {
        val startDateTime = LocalDateTime.parse(timeEntry.date.toString())
        timeEntries.postTimeEntry(PostTimeEntry(
            true,
            null,
            startDateTime.plusHours(timeEntry.hours.toLong()),
            "projectId",
            startDateTime,
            null,
            timeEntry.taskId
        ))
    }
}
