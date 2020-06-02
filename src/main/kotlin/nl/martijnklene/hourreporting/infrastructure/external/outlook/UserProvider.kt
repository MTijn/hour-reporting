package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.models.extensions.User
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UserProvider(private val graphServiceClientProvider: GraphClientProvider) {
    fun findOutlookUser(authentication: Authentication): User {
        return graphServiceClientProvider.provideGraphClient(authentication).me().buildRequest().get()
    }
}
