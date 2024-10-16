package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.controllers.response.PostedHours
import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.tempo.dto.WorkLog
import nl.martijnklene.hourreporting.tempo.service.WorkLogPoster
import org.springframework.stereotype.Component
import java.time.*

@Component
class HoursPoster(
    private val workLogPoster: WorkLogPoster
) {
    fun createTimeEntries(
        postedParams: PostedHours,
        user: User
    ) {
        if (postedParams.hours.isNullOrEmpty()) {
            throw RuntimeException("Missing hours to be posted")
        }

        postedParams.hours!!.forEach {
            val postedDescriptions = it.value["projectDescription"]?.split(",").orEmpty()
            val postedTaskIds = it.value["taskId"]?.split(",").orEmpty()

            it.value["hours"]?.split(",").orEmpty().forEachIndexed { index, postedHour ->
                val duration = Duration.ofMinutes(postedHour.toLong())

                workLogPoster.postWorkLogItem(
                    user.jiraApiKey,
                    postedTaskIds[index],
                    WorkLog(
                        timeSpentSeconds = duration.toSeconds().toInt(),
                        comment = postedDescriptions[index],
                        started =
                            ZonedDateTime.ofInstant(
                                LocalDate
                                    .parse(it.key)
                                    .atStartOfDay()
                                    .toInstant(ZoneOffset.UTC),
                                ZoneId.of("Europe/Amsterdam")
                            )
                    )
                )
            }
        }
    }
}
