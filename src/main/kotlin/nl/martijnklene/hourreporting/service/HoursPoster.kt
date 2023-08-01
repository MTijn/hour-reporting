package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.controllers.dto.FormEntity
import nl.martijnklene.hourreporting.tempo.dto.WorkLog
import nl.martijnklene.hourreporting.tempo.service.WorkLogPoster
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate

@Component
class HoursPoster(
    private val workLogPoster: WorkLogPoster
) {
    fun createTimeEntries(postedParams: FormEntity, apiKey: String) {
        if (postedParams.hours.isNullOrEmpty()) {
            throw RuntimeException("Missing hours to be posted")
        }

        postedParams.hours!!.forEach {
            val postedDescriptions = it.value["projectDescription"]?.split(",").orEmpty()
            val postedTaskIds = it.value["taskId"]?.split(",").orEmpty()

            it.value["hours"]?.split(",").orEmpty().forEachIndexed { index, postedHour ->
                val duration = Duration.ofMinutes(postedHour.toLong())

                workLogPoster.postWorkLogItem(
                    apiKey,
                    WorkLog(
                        timeSpentSeconds = duration.toSeconds().toInt(),
                        comment = postedDescriptions[index],
                        originTaskId = postedTaskIds[index],
                        worker = "martijnk",
                        started = LocalDate.parse(it.key)
                    )
                )
            }
        }
    }
}
