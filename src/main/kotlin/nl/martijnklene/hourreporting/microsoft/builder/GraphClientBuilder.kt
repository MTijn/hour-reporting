package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.graph.serviceclient.GraphServiceClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component

@Component
class GraphClientBuilder(private val provider: GraphAuthenticationProvider) {
    fun buildGraphClient(client: OAuth2AuthorizedClient): GraphServiceClient {
        val item = provider.build(client)
        return GraphServiceClient(item)
    }
}
