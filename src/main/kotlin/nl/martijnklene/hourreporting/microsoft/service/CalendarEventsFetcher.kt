package nl.martijnklene.hourreporting.microsoft.service

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
    ): EventCollectionPage? {
        val options = LinkedList<Option>()
        options.add(
            QueryOption(
                "startDateTime",
                date.atTime(7, 0).atZone(ZoneId.of("Europe/Amsterdam")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
        )
        options.add(
            QueryOption(
                "endDateTime",
                date.atTime(19, 0).atZone(ZoneId.of("Europe/Amsterdam")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
        )
        return graphClientBuilder.buildGraphClient(client).me().calendarView().buildRequest(options).get()
    }
}
