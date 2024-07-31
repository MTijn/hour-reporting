package nl.martijnklene.hourreporting.microsoft.service

import com.microsoft.graph.core.tasks.PageIterator
import com.microsoft.graph.models.Event
import com.microsoft.graph.models.EventCollectionResponse
import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


@Component
class CalendarEventsFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun getEventsForADay(
        date: LocalDate,
        client: OAuth2AuthorizedClient
    ): List<Event> {
        val events = mutableListOf<Event>()
        val graphClient = graphClientBuilder.buildGraphClient(client)
        val response = graphClient.me().calendarView().get { requestConfiguration ->
            requestConfiguration.queryParameters.startDateTime =
                date.atTime(7, 0).atZone(ZoneId.of("Europe/Amsterdam"))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            requestConfiguration.queryParameters.endDateTime =
                date.atTime(19, 0).atZone(ZoneId.of("Europe/Amsterdam"))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        PageIterator.Builder<Event, EventCollectionResponse>()
            .client(graphClient)
            .collectionPage(Objects.requireNonNull(response))
            .collectionPageFactory(EventCollectionResponse::createFromDiscriminatorValue)
            .processPageItemCallback { event ->
                events.add(event)
            }
            .build()
            .iterate()
        return events
    }
}
