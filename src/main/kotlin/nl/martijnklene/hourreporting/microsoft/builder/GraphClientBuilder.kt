package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component

@Component
class GraphClientBuilder(private val provider: GraphAuthenticationProvider) {
    fun buildGraphClient(client: OAuth2AuthorizedClient): GraphServiceClient<Request> {
        val item = provider.build(client)
        return GraphServiceClient.builder().authenticationProvider(item).buildClient()
    }
}
