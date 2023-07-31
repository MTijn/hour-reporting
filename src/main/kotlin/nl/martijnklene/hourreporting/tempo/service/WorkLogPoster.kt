package nl.martijnklene.hourreporting.tempo.service

import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.tempo.dto.WorkLog
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity
import org.springframework.stereotype.Component

@Component
class WorkLogPoster(
    private val encryption: StringEncryption,
    private val objectMapper: ObjectMapper
) {
    fun postWorkLogItem(token: String, item: WorkLog) {
        val httpClient = HttpClients.createDefault()
        val request = HttpPost("https://jira.voiceworks.com/rest/tempo-timesheets/4/worklogs")
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${encryption.decryptText(token)}")
        val requestJson = objectMapper.writeValueAsString(item)
        request.entity = StringEntity(
            requestJson,
            ContentType.APPLICATION_JSON,
        )
        val response = httpClient.execute(request)
        httpClient.close()
    }
}
