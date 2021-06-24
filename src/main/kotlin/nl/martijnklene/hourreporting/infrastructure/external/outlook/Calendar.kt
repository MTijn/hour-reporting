package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.options.Option
import com.microsoft.graph.options.QueryOption
import com.microsoft.graph.requests.EventCollectionPage
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class Calendar(private val graphServiceClientProvider: GraphClientProvider) {
    fun getEventsForADay(authentication: Authentication, date: LocalDate): EventCollectionPage {
        val options = LinkedList<Option>()
        options.add(QueryOption("startDateTime", date.atTime(7, 0).atZone(ZoneId.of("Europe/Amsterdam")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
        options.add(QueryOption("endDateTime", date.atTime(19, 0).atZone(ZoneId.of("Europe/Amsterdam")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))

        return graphServiceClientProvider.provideGraphClient(authentication)
            .me()
            .calendarView()
            .buildRequest(options)
            .get()
    }
}
