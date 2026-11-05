package nl.martijnklene.hourreporting.support

import nl.martijnklene.hourreporting.model.Category
import nl.martijnklene.hourreporting.model.IgnoredCategory
import nl.martijnklene.hourreporting.model.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import java.time.Instant
import java.util.*

val USER_ID: UUID = UUID.fromString("5f0c7a52-3a3e-4d1c-9c55-3d0f7b3a1e11")

fun aUser(
    categories: List<Category> = emptyList(),
    ignoredCategories: List<IgnoredCategory> = emptyList()
) = User(
    id = USER_ID,
    name = "Test User",
    categories = categories,
    ignoredCategories = ignoredCategories,
    jiraApiKey = "encrypted-jira-key",
    tempoApiKey = "encrypted-tempo-key",
    jiraUserName = "test.user@example.com",
    jiraAccountId = "jira-account-id"
)

fun aCategory(
    name: String,
    jiraKey: String,
    id: Int,
    default: Boolean = false
) = Category(
    userId = USER_ID,
    categoryName = name,
    jiraKey = jiraKey,
    id = id,
    default = default
)

fun anAuthorizedClient(token: String = "access-token"): OAuth2AuthorizedClient {
    val registration =
        ClientRegistration
            .withRegistrationId("graph")
            .clientId("client-id")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("https://localhost/login/oauth2/code/graph")
            .authorizationUri("https://localhost/authorize")
            .tokenUri("https://localhost/token")
            .build()
    val accessToken =
        OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            token,
            Instant.now(),
            Instant.now().plusSeconds(3600)
        )
    return OAuth2AuthorizedClient(registration, "Test User", accessToken)
}

fun authenticateAs(
    oid: UUID = USER_ID,
    name: String = "Test User"
) {
    val principal =
        DefaultOAuth2User(
            listOf(SimpleGrantedAuthority("ROLE_USER")),
            mapOf("oid" to oid.toString(), "name" to name),
            "name"
        )
    SecurityContextHolder.getContext().authentication =
        OAuth2AuthenticationToken(principal, principal.authorities, "graph")
}
