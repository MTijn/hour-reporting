package nl.martijnklene.hourreporting.infrastructure.repository

import com.microsoft.graph.models.WorkingHours
import nl.martijnklene.hourreporting.infrastructure.external.outlook.GraphClientProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Repository

@Repository
class WorkingHoursHttpRepository(private val graphServiceClientProvider: GraphClientProvider) {
    fun findWorkingHoursFromAuthentication(authentication: Authentication): WorkingHours? {
        return graphServiceClientProvider.provideGraphClient()
            .customRequest("/me/mailboxSettings/workingHours", WorkingHours::class.java)
            .buildRequest()
            .get()
    }
}
