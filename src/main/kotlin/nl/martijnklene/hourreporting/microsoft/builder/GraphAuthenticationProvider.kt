package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.graph.authentication.BaseAuthenticationProvider
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Service
import java.net.URL
import java.util.concurrent.CompletableFuture

@Service
class GraphAuthenticationProvider() : BaseAuthenticationProvider() {
    private lateinit var client: OAuth2AuthorizedClient
    override fun getAuthorizationTokenAsync(url: URL): CompletableFuture<String> {
        return CompletableFuture.completedFuture(client.accessToken.tokenValue)
    }

    fun build(client: OAuth2AuthorizedClient): GraphAuthenticationProvider {
        this.client = client
        return this
    }
}
