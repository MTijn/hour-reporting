package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.options.Option
import com.microsoft.graph.options.QueryOption
import com.microsoft.graph.requests.extensions.GraphServiceClient
import com.microsoft.graph.requests.extensions.IEventCollectionPage
import nl.martijnklene.hourreporting.infrastructure.configuration.SimpleAuthProvider
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class Calendar() {
    fun getEventsForADay(date: LocalDate): IEventCollectionPage {
        val authentication = SecurityContextHolder.getContext().authentication
        val details: OAuth2AuthenticationDetails = authentication.details as OAuth2AuthenticationDetails
        val token = details.tokenValue

        val authProvider = SimpleAuthProvider(token)
        val graphClient = GraphServiceClient
            .builder()
            .authenticationProvider(authProvider)
            .buildClient()

        val options = LinkedList<Option>()
        // Sort results by createdDateTime, get newest first
        options.add(QueryOption("orderby", "start/dateTime ASC"));
        options.add(QueryOption("startDateTime", date.atStartOfDay()))
        options.add(QueryOption("endDateTime", date.atTime(23, 59)))

        // GET /me/events
        return graphClient
            .me()
            .calendarView()
            .buildRequest(options)
            .select("subject,organizer,start,end")
            .get()
    }
}
