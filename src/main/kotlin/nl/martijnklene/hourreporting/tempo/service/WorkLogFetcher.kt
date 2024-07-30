package nl.martijnklene.hourreporting.tempo.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.Unirest
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.tempo.dto.WorkLogRequest
import nl.martijnklene.hourreporting.tempo.model.WorkLogs
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class WorkLogFetcher(
    private val objectMapper: ObjectMapper,
    private val encryption: StringEncryption
) {
    fun fetchWorkLogsBetweenDates(
        start: LocalDate,
        end: LocalDate,
        tempoUser: User
    ): WorkLogs {
        val worksLogs =
            Unirest
                .post("https://api.tempo.io/4/worklogs/search?limit=500")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer ${encryption.decryptText(tempoUser.tempoApiKey)}")
                .body(
                    objectMapper.writeValueAsString(
                        WorkLogRequest(
                            from = start.format(DateTimeFormatter.ISO_DATE),
                            to = end.format(DateTimeFormatter.ISO_DATE),
                            authorIds = listOf(tempoUser.jiraAccountId)
                        )
                    )
                )
                .asJson()
                .body

        return objectMapper.readValue<WorkLogs>(worksLogs.toString())
    }
}
