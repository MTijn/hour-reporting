package nl.martijnklene.hourreporting.infrastructure.service

import nl.martijnklene.hourreporting.infrastructure.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.infrastructure.external.clockify.Projects
import nl.martijnklene.hourreporting.infrastructure.external.outlook.Calendar
import nl.martijnklene.hourreporting.infrastructure.external.outlook.CategoryMapper
import nl.martijnklene.hourreporting.infrastructure.external.outlook.IgnoredCategories
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class HoursSuggestionCalculator(
    private val calendar: Calendar,
    private val categoryMapper: CategoryMapper,
    private val ignoredCategories: IgnoredCategories,
    private val projects: Projects,
    private val datesSuggester: DatesProvider
) {
    fun suggestHoursForADay(authentication: Authentication, date: LocalDate): SuggestedTimeEntry {
        var workingHours = Duration.parse("PT8H")
        var taskId = ""
        for (event in calendar.getEventsForADay(authentication, date).currentPage) {
            if (event.responseStatus.response.name != "ACCEPTED" && event.responseStatus.response.name != "ORGANIZER") {
                continue
            }
            val category = event.categories.firstOrNull()

            if (event.isAllDay) {
                taskId = categoryMapper.mapCategoryToClockifyTaskId(category)
                workingHours = Duration.parse("PT8H")
                break
            }

            taskId = categoryMapper.mapCategoryToClockifyTaskId(category)
            if (ignoredCategories.shouldCategoryBeIgnored(category)) {
                continue
            }
            workingHours = workingHours.minus(Duration.between(
                LocalDateTime.parse(event.start.dateTime),
                LocalDateTime.parse(event.end.dateTime)
            ))
        }

        val identifiedTask = projects.projectsForUser().flatMap { it.tasks }.first { task -> task.id == taskId}
        return SuggestedTimeEntry(
            workingHours,
            taskId,
            identifiedTask.name,
            date
        );
    }

    fun suggestHoursForAnAuthenticatedUser(authentication: Authentication): List<SuggestedTimeEntry> {
        val dates = datesSuggester.suggestDaysForTheAuthenticatedUser(authentication)
        val suggestedEntries = mutableListOf<SuggestedTimeEntry>()
        dates.forEach { it -> suggestedEntries.add(this.suggestHoursForADay(authentication, it))}
        return suggestedEntries
    }
}
