package nl.martijnklene.hourreporting.microsoft.service

import com.microsoft.graph.models.WorkingHours
import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component

@Component
class WorkingHoursFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun findWorkingHoursFromAuthenticatedUser(client: OAuth2AuthorizedClient): WorkingHours? {
        val graphClient = graphClientBuilder.buildGraphClient(client)
        val response = graphClient.me().mailboxSettings().get()

        return response.workingHours
    }
}
