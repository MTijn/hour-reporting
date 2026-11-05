package nl.martijnklene.hourreporting.microsoft.builder

import nl.martijnklene.hourreporting.support.anAuthorizedClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.net.URI

class GraphAuthenticationProviderTest {
    private val provider = GraphAuthenticationProvider()

    @Test
    fun `hands out the access token of the authorized client`() {
        val built = provider.build(anAuthorizedClient("token-123"))

        assertSame(provider, built)
        assertEquals("token-123", built.getAuthorizationToken(URI("https://graph.microsoft.com/v1.0/me"), null))
    }

    @Test
    fun `provides an allowed hosts validator`() {
        assertNotNull(provider.allowedHostsValidator)
    }
}
