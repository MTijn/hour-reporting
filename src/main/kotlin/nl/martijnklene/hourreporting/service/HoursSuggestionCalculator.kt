package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.controllers.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.microsoft.service.CalendarEventsFetcher
import nl.martijnklene.hourreporting.model.User
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
    fun suggestHoursForADay(
        date: LocalDate,
        client: OAuth2AuthorizedClient,
        user: User
    ): Collection<SuggestedTimeEntry> {
        var durationToEnterIntoTempo = Duration.parse("PT8H")
        val suggestedTimeEntries = mutableListOf<SuggestedTimeEntry>()

        workLogFetcher.fetchWorkLogsBetweenDates(date, date, user).results.forEach {
            durationToEnterIntoTempo = durationToEnterIntoTempo.minus(Duration.ofSeconds(it.timeSpentSeconds.toLong()))
        }

        calendarService.getEventsForADay(date, client).forEach { event ->
            if (durationToEnterIntoTempo.isZero || durationToEnterIntoTempo.isNegative) {
                return@forEach
            }
            if (event.responseStatus!!.response!!.name != "Accepted" &&
                event.responseStatus!!.response!!.name != "Organizer"
            ) {
                return@forEach
            }

            val category = event.categories!!.firstOrNull()

            if (event.isAllDay == true) {
                suggestedTimeEntries.add(
                    SuggestedTimeEntry(
                        durationToEnterIntoTempo,
                        categoryMapper.mapCategoryToJiraTaskId(category, user) ?: 0,
                        categoryMapper.mapCategoryToJiraKey(category, user) ?: "",
                        event.subject.toString(),
                        LocalDateTime.parse(event.start!!.dateTime).toLocalDate()
                    )
                )
                durationToEnterIntoTempo = durationToEnterIntoTempo.minus(durationToEnterIntoTempo)
                return@forEach
            }

            val taskId = categoryMapper.mapCategoryToJiraTaskId(category, user) ?: 0
            val taskKey = categoryMapper.mapCategoryToJiraKey(category, user) ?: ""
            if (ignoredCategories.shouldCategoryBeIgnored(category, user)) {
                return@forEach
            }

            var duration =
                Duration.between(
                    LocalDateTime.parse(event.start!!.dateTime),
                    LocalDateTime.parse(event.end!!.dateTime)
                )

            if (durationToEnterIntoTempo < duration) {
                duration = durationToEnterIntoTempo
            }

            if (taskId != null) {
                suggestedTimeEntries.add(
                    SuggestedTimeEntry(
                        duration,
                        taskId.or(0),
                        taskKey.orEmpty(),
                        event.subject.toString(),
                        LocalDateTime.parse(event.start!!.dateTime).toLocalDate()
                    )
                )
            }

            durationToEnterIntoTempo = durationToEnterIntoTempo.minus(duration)
        }

        if (durationToEnterIntoTempo.isNegative || durationToEnterIntoTempo.isZero) {
            return suggestedTimeEntries
        }

        suggestedTimeEntries.add(
            SuggestedTimeEntry(
                durationToEnterIntoTempo,
                categoryMapper.mapCategoryToJiraTaskId("", user) ?: 0,
                categoryMapper.mapCategoryToJiraKey("", user) ?: "",
                "Meeting",
                date
            )
        )

        return suggestedTimeEntries
    }

    fun suggestHoursForAnAuthenticatedUser(
        user: User,
        client: OAuth2AuthorizedClient
    ): List<SuggestedTimeEntry> {
        val dates = datesSuggesterService.suggestDaysForTheAuthenticatedUser(user.jiraApiKey, client)
        val suggestedEntries = mutableListOf<SuggestedTimeEntry>()
        dates.forEach {
            this
                .suggestHoursForADay(it, client, user)
                .forEach { suggestedTimeEntry -> suggestedEntries.add(suggestedTimeEntry) }
        }
        return suggestedEntries
    }
}
