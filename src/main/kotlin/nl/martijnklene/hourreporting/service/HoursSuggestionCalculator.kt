package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.microsoft.service.CalendarEventsFetcher
import nl.martijnklene.hourreporting.tempo.service.WorkLogFetcher
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class HoursSuggestionCalculator(
    private val calendarService: CalendarEventsFetcher,
    private val categoryMapper: CategoryMapper,
    private val ignoredCategories: IgnoredCategories,
    private val datesSuggesterService: DatesSuggesterService,
    private val workLogFetcher: WorkLogFetcher
) {
    fun suggestHoursForADay(date: LocalDate, client: OAuth2AuthorizedClient, apiKey: String): Collection<SuggestedTimeEntry> {
        var durationToEnterIntoTempo = Duration.parse("PT8H")
        val suggestedTimeEntries = mutableListOf<SuggestedTimeEntry>()
        workLogFetcher.fetchWorkLogsBetweenDates(date, date, apiKey).forEach {
            durationToEnterIntoTempo = durationToEnterIntoTempo.minus(Duration.ofSeconds(it.billableSeconds.toLong()))
        }
        for (event in calendarService.getEventsForADay(date, client)!!.currentPage) {
            if (durationToEnterIntoTempo.isZero || durationToEnterIntoTempo.isNegative) {
                continue
            }
            if (event.responseStatus!!.response!!.name != "ACCEPTED" && event.responseStatus!!.response!!.name != "ORGANIZER") {
                continue
            }
            val category = event.categories!!.firstOrNull()

            if (event.isAllDay == true) {
                durationToEnterIntoTempo = durationToEnterIntoTempo.minus(Duration.parse("PT8H"))
                suggestedTimeEntries.add(
                    SuggestedTimeEntry(
                        Duration.parse("PT8H"),
                        categoryMapper.mapCategoryToJiraTaskId(category),
                        event.subject.toString(),
                        LocalDateTime.parse(event.start!!.dateTime).toLocalDate()
                    )
                )
                break
            }

            val taskId = categoryMapper.mapCategoryToJiraTaskId(category)
            if (ignoredCategories.shouldCategoryBeIgnored(category) || event.subject == "Focus time") {
                continue
            }

            val duration = Duration.between(
                LocalDateTime.parse(event.start!!.dateTime),
                LocalDateTime.parse(event.end!!.dateTime)
            )

            suggestedTimeEntries.add(
                SuggestedTimeEntry(
                    duration,
                    taskId,
                    event.subject.toString(),
                    LocalDateTime.parse(event.start!!.dateTime).toLocalDate()
                )
            )

            durationToEnterIntoTempo = durationToEnterIntoTempo.minus(duration)
        }

        if (durationToEnterIntoTempo.isNegative || durationToEnterIntoTempo.isZero) {
            return suggestedTimeEntries
        }

        suggestedTimeEntries.add(
            SuggestedTimeEntry(
                durationToEnterIntoTempo,
                categoryMapper.mapCategoryToJiraTaskId(""),
                "Meeting",
                date
            )
        )

        return suggestedTimeEntries
    }

    fun suggestHoursForAnAuthenticatedUser(apiKey: String, client: OAuth2AuthorizedClient): List<SuggestedTimeEntry> {
        val dates = datesSuggesterService.suggestDaysForTheAuthenticatedUser(apiKey, client)
        val suggestedEntries = mutableListOf<SuggestedTimeEntry>()
        dates.forEach { this.suggestHoursForADay(it, client, apiKey).forEach { suggestedEntries.add(it) } }
        return suggestedEntries
    }
}
