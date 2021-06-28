package nl.martijnklene.hourreporting.clockify.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.clockify.model.Project
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Value

class ProjectsService(
    @Value("\${clockify.api-key}") private val apiKey: String,
    @Value("\${clockify.workspace}") private val workSpace: String,
    @Value("\${clockify.user}") private val user: String,
    private val objectMapper: ObjectMapper
) {
    private var clockifyProjects: MutableList<Project> = mutableListOf()

    fun projectsForUser(): List<Project> {
        if (clockifyProjects.isNotEmpty()) {
            return clockifyProjects
        }

        val httpClient = HttpClients.createDefault()
        val request = HttpGet(String.format(
            "https://global.api.clockify.me/workspaces/%s/projects/user/%s/filter?page=1&search=&clientId=&include=ONLY_NOT_FAVORITES",
            workSpace,
            user
        ))
        request.addHeader("X-API-KEY", apiKey)
        val response = httpClient.execute(request)

        val toString = EntityUtils.toString(response.entity)
        clockifyProjects = objectMapper.readValue(
            toString,
            object : TypeReference<MutableList<Project>>() {}
        );
        return clockifyProjects
    }
}
