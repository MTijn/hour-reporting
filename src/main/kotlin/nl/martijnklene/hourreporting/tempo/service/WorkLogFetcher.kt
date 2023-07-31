package nl.martijnklene.hourreporting.tempo.service

import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.tempo.dto.WorkLogRequest
import nl.martijnklene.hourreporting.tempo.model.WorkLog
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class WorkLogFetcher(
    private val objectMapper: ObjectMapper,
    private val encryption: StringEncryption
) {
    fun fetchWorkLogsBetweenDates(from: LocalDate, to: LocalDate, token: String): Collection<WorkLog> {
        val httpClient = HttpClients.createDefault()
        val request = HttpPost("https://jira.voiceworks.com/rest/tempo-timesheets/4/worklogs/search")
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${encryption.decryptText(token)}")
        val requestJson = objectMapper.writeValueAsString(
            WorkLogRequest(
                from.format(DateTimeFormatter.ISO_LOCAL_DATE),
                to.format(DateTimeFormatter.ISO_LOCAL_DATE),
                listOf("martijnk")
            )
        )
        request.entity = StringEntity(
            requestJson,
            ContentType.APPLICATION_JSON,
        )
        val response = httpClient.execute(request)
        if (response.code != 200) {
            return emptyList()
        }

        return objectMapper.readValue(
            response.entity.content,
            objectMapper.typeFactory.constructCollectionType(
                MutableList::class.java,
                WorkLog::class.java
            )
        )
    }
}
