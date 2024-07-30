package nl.martijnklene.hourreporting.tempo.service

import com.fasterxml.jackson.databind.ObjectMapper
import kong.unirest.Unirest
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.tempo.dto.WorkLog
import org.springframework.stereotype.Component

@Component
class WorkLogPoster(
    private val encryption: StringEncryption,
    private val objectMapper: ObjectMapper
) {
    fun postWorkLogItem(
        token: String,
        issueKey: String,
        item: WorkLog,
    ) {
        val response =
            Unirest.post("https://enreach-services.atlassian.net/rest/api/2/issue/$issueKey/worklog")
                .basicAuth("martijn.klene@enreach.com", encryption.decryptText(token))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(item))
                .asJson()
        val result = response.body
    }
}
