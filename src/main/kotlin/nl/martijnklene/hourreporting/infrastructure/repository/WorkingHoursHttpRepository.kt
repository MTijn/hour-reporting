package nl.martijnklene.hourreporting.infrastructure.repository

import com.microsoft.graph.models.extensions.WorkingHours
import nl.martijnklene.hourreporting.infrastructure.external.outlook.GraphClientProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Repository

@Repository
class WorkingHoursHttpRepository(private val graphServiceClientProvider: GraphClientProvider) {
    fun findWorkingHoursFromAuthentication(authentication: Authentication): WorkingHours {
        return graphServiceClientProvider.provideGraphClient(authentication)
            .customRequest("/me/mailboxSettings/workingHours", WorkingHours::class.java)
            .buildRequest()
            .get()
    }
}
