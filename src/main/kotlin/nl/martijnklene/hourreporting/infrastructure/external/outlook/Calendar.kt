package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.options.Option
import com.microsoft.graph.options.QueryOption
import com.microsoft.graph.requests.extensions.IEventCollectionPage
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class Calendar(private val graphServiceClientProvider: GraphClientProvider) {
    fun getEventsForADay(date: LocalDate): IEventCollectionPage {
        val options = LinkedList<Option>()
        options.add(QueryOption("startDateTime", date.atStartOfDay()))
        options.add(QueryOption("endDateTime", date.atTime(23, 59)))

        return graphServiceClientProvider.provideGraphClient()
            .me()
            .calendarView()
            .buildRequest(options)
            .get()
    }
}
