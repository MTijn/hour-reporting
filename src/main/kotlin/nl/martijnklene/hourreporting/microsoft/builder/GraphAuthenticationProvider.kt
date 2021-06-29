package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.graph.authentication.BaseAuthenticationProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import java.net.URL
import java.util.concurrent.CompletableFuture

@Service
class GraphAuthenticationProvider() : BaseAuthenticationProvider() {
    override fun getAuthorizationTokenAsync(url: URL): CompletableFuture<String> {
        val context = SecurityContextHolder.getContext()
        val token = context.authentication as OAuth2AuthenticationToken
        return CompletableFuture.completedFuture("test")
    }
}
