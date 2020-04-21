package nl.martijnklene.hourreporting.infrastructure.external.outlook

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Calendar() {
    fun getEventsForADay(date: LocalDate) {
        val authentication = SecurityContextHolder.getContext().authentication
        val details: OAuth2AuthenticationDetails = authentication.details as OAuth2AuthenticationDetails
        val token = details.tokenValue

        val url = "https://graph.microsoft.com/v1.0/me/calendar/events"
        val client: CloseableHttpClient = HttpClients.createDefault();

        val request = HttpGet(url)
        request.setHeader("Authorization", "Bearer $token")
        val response = client.execute(request)
        client.close()

        print(response)
    }
}
