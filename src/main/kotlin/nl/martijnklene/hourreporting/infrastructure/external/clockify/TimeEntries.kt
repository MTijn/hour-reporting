package nl.martijnklene.hourreporting.infrastructure.external.clockify

import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.PostTimeEntry
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.TimeEntriesList
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.TimeEntry
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class TimeEntries(
    @Value("\${clockify.api-key}") private val apiKey: String,
    @Value("\${clockify.workspace}") private val workSpace: String,
    @Value("\${clockify.user}") private val user: String,
    private val objectMapper: ObjectMapper
) {
    fun lastClockifyTimeEntry(): TimeEntry? {
        val httpClient = HttpClients.createDefault()
        val request = HttpGet(String.format(
            "https://global.api.clockify.me/workspaces/%s/timeEntries/user/%s/full?page=0&limit=1",
            workSpace,
            user
        ))
        request.addHeader("X-API-KEY", apiKey)
        val response = httpClient.execute(request)
        httpClient.close()

        val entries = objectMapper.readValue(
            EntityUtils.toString(response.entity),
            TimeEntriesList::class.java
        );

        return entries.timeEntriesList.stream().findFirst().get();
    }

    fun postTimeEntry(timeEntry: PostTimeEntry) {
        val httpClient = HttpClients.createDefault()
        val request = HttpPost(
            String.format(
                "https://global.api.clockify.me/workspaces/%s/timeEntries/users/%s/full",
                workSpace,
                user
            )
        )
        request.entity = StringEntity(objectMapper.writeValueAsString(timeEntry))
        request.addHeader("X-API-KEY", apiKey)
        request.addHeader("content-type", "application/json")
        val response = httpClient.execute(request)
        httpClient.close()
    }
}
