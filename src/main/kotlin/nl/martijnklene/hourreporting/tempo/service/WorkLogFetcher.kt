package nl.martijnklene.hourreporting.tempo.service

import kong.unirest.core.Unirest
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.tempo.dto.WorkLogRequest
import nl.martijnklene.hourreporting.tempo.model.WorkLogs
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class WorkLogFetcher(
    private val objectMapper: JsonMapper,
    private val encryption: StringEncryption
) {
    fun fetchWorkLogsBetweenDates(
        start: LocalDate,
        end: LocalDate,
        tempoUser: User
    ): WorkLogs {
        val response =
            Unirest
                .post("https://api.eu.tempo.io/4/worklogs/search?limit=500")
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
                ).asJson()

        if (response.status == 400 || response.status == 401) {
            return WorkLogs(
                self = "",
                metadata = LinkedHashMap(),
                results = emptyList()
            )
        }

        return objectMapper.readValue<WorkLogs>(response.body.toString())
    }
}
