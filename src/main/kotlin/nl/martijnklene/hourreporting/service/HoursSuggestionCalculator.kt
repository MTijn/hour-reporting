package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.clockify.service.ProjectsService
import nl.martijnklene.hourreporting.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.microsoft.service.CalendarEventsFetcher
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class HoursSuggestionCalculator(
    private val projectsService: ProjectsService,
    private val calendarService: CalendarEventsFetcher,
    private val categoryMapper: CategoryMapper,
    private val ignoredCategories: IgnoredCategories,
    private val datesSuggesterService: DatesSuggesterService
) {
    fun suggestHoursForADay(date: LocalDate, client: OAuth2AuthorizedClient): SuggestedTimeEntry {
        var workingHours = Duration.parse("PT8H")
        var taskId = ""
        for (event in calendarService.getEventsForADay(date, client)!!.currentPage) {
            if (event.responseStatus!!.response!!.name != "ACCEPTED" && event.responseStatus!!.response!!.name != "ORGANIZER") {
                continue
            }
            val category = event.categories!!.firstOrNull()

            if (event.isAllDay == true) {
                taskId = categoryMapper.mapCategoryToJiraTaskId(category)
                workingHours = Duration.parse("PT8H")
                break
            }

            taskId = categoryMapper.mapCategoryToJiraTaskId(category)
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

    fun suggestHoursForAnAuthenticatedUser(apiKey: String, client: OAuth2AuthorizedClient): List<SuggestedTimeEntry> {
        val dates = datesSuggesterService.suggestDaysForTheAuthenticatedUser(apiKey, client)
        val suggestedEntries = mutableListOf<SuggestedTimeEntry>()
        dates.forEach { suggestedEntries.add(this.suggestHoursForADay(it, client))}
        return suggestedEntries
    }
}
