package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.models.extensions.IGraphServiceClient
import com.microsoft.graph.requests.extensions.GraphServiceClient
import nl.martijnklene.hourreporting.infrastructure.configuration.SimpleAuthProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.stereotype.Component

@Component
class GraphClientProvider {
    fun provideGraphClient(): IGraphServiceClient {
        val authentication = SecurityContextHolder.getContext().authentication
        val details: OAuth2AuthenticationDetails = authentication.details as OAuth2AuthenticationDetails
        val token = details.tokenValue

        val authProvider = SimpleAuthProvider(token)
        return GraphServiceClient
            .builder()
            .authenticationProvider(authProvider)
            .buildClient()
    }
}
