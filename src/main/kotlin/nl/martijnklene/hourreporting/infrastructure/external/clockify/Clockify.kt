package nl.martijnklene.hourreporting.infrastructure.external.clockify

import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.TimeEntriesList
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.TimeEntry
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class Clockify(
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

        val entries = objectMapper.readValue(
            EntityUtils.toString(response.entity),
            TimeEntriesList::class.java
        );

        return entries.timeEntriesList.stream().findFirst().get();
    }
}
