package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.kiota.authentication.AccessTokenProvider
import com.microsoft.kiota.authentication.AllowedHostsValidator
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Service
import java.net.URI

@Service
class GraphAuthenticationProvider() : AccessTokenProvider {
    private lateinit var client: OAuth2AuthorizedClient

    override fun getAuthorizationToken(
        uri: URI,
        additionalAuthenticationContext: MutableMap<String, Any>?
    ): String {
        return client.accessToken.tokenValue
    }

    override fun getAllowedHostsValidator(): AllowedHostsValidator {
        return AllowedHostsValidator()
    }

    fun build(client: OAuth2AuthorizedClient): GraphAuthenticationProvider {
        this.client = client
        return this
    }
}
