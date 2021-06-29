package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.clockify.service.ProjectsService
import nl.martijnklene.hourreporting.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.microsoft.service.CalendarEventsFetcher
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class HoursSuggestionCalculator(
    private val projectsService: ProjectsService,
    private val calendarService: CalendarEventsFetcher,
    private val categoryMapper: CategoryMapper,
    private val ignoredCategories: IgnoredCategories,
    private val datesSuggesterService: DatesSuggesterService
) {
    fun suggestHoursForADay(date: LocalDate): SuggestedTimeEntry {
        var workingHours = Duration.parse("PT8H")
        var taskId = ""
        for (event in calendarService.getEventsForADay(date)!!.currentPage) {
            if (event.responseStatus!!.response!!.name != "ACCEPTED" && event.responseStatus!!.response!!.name != "ORGANIZER") {
                continue
            }
            val category = event.categories!!.firstOrNull()

            if (event.isAllDay == true) {
                taskId = categoryMapper.mapCategoryToClockifyTaskId(category)
                workingHours = Duration.parse("PT8H")
                break
            }

            taskId = categoryMapper.mapCategoryToClockifyTaskId(category)
            if (ignoredCategories.shouldCategoryBeIgnored(category)) {
                continue
            }
            workingHours = workingHours.minus(Duration.between(
                LocalDateTime.parse(event.start!!.dateTime),
                LocalDateTime.parse(event.end!!.dateTime)
            ))
        }

        val identifiedTask = projectsService.projectsForUser().flatMap { it.tasks }.first { task -> task.id == taskId}
        return SuggestedTimeEntry(
            workingHours,
            taskId,
            identifiedTask.name,
            date
        );
    }

    fun suggestHoursForAnAuthenticatedUser(apiKey: String): List<SuggestedTimeEntry> {
        val dates = datesSuggesterService.suggestDaysForTheAuthenticatedUser(apiKey)
        val suggestedEntries = mutableListOf<SuggestedTimeEntry>()
        dates.forEach { suggestedEntries.add(this.suggestHoursForADay(it))}
        return suggestedEntries
    }
}
