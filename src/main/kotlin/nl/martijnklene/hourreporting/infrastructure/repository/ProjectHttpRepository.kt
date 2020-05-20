package nl.martijnklene.hourreporting.infrastructure.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.infrastructure.external.clockify.dto.Project
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProjectHttpRepository(
    @Value("\${clockify.api-key}") private val apiKey: String,
    @Value("\${clockify.workspace}") private val workSpace: String,
    @Value("\${clockify.user}") private val user: String,
    private val objectMapper: ObjectMapper
) {
    private var projects = mutableListOf<Project>()

    fun getAllProjects(): List<Project> {
        if (projects.isNotEmpty()) {
            return projects
        }

        val httpClient = HttpClients.createDefault()
        val request = HttpGet(
            String.format(
                "https://global.api.clockify.me/workspaces/%s/projects/user/%s/filter?page=1&search=&clientId=&include=ONLY_NOT_FAVORITES",
                workSpace,
                user
            )
        )
        request.addHeader("X-API-KEY", apiKey)
        val response = httpClient.execute(request)

        val toString = EntityUtils.toString(response.entity)
        projects = objectMapper.readValue(
            toString,
            object : TypeReference<MutableList<Project>>() {}
        );
        return projects
    }

    fun getProjectFromTaskId(taskId: String): Project? {
        if (projects.isEmpty()) {
            getAllProjects()
        }

        projects.forEach {
            val task = it.tasks.find { task -> task.id.equals(taskId) }
            if (task != null) {
                return it
            }
        }
        return null
    }
}