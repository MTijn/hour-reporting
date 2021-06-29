package nl.martijnklene.hourreporting.microsoft.service

import com.microsoft.graph.models.WorkingHours
import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.stereotype.Component

@Component
class WorkingHoursFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun findWorkingHoursFromAuthenticatedUser(): WorkingHours? {
        return graphClientBuilder.buildGraphClient()
            .customRequest("/me/mailboxSettings/workingHours", WorkingHours::class.java)
            .buildRequest()
            .get()
    }
}
