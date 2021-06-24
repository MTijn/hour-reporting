package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.models.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserProvider(private val graphServiceClientProvider: GraphClientProvider) {
    fun findOutlookUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        return graphServiceClientProvider.provideGraphClient(authentication).me().buildRequest().get()
    }
}
