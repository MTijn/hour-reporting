package nl.martijnklene.hourreporting.microsoft.service

import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class UserPhotoFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun fetchPhotoFromLoggedInUser(client: OAuth2AuthorizedClient): String {
        return Base64.getEncoder().encodeToString(
            graphClientBuilder.buildGraphClient(client).me().photo().content().buildRequest().get()?.readAllBytes()
        )
    }
}
