package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.controllers.response.PostedHours
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.service.HoursPoster
import nl.martijnklene.hourreporting.service.HoursSuggestionCalculator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class HomeController(
    private val userRepository: UserRepository,
    private val hoursPoster: HoursPoster,
    private val hoursSuggestionCalculator: HoursSuggestionCalculator
) {
    @GetMapping("/")
    fun homeScreen(
        model: ModelMap,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
            ?: return RedirectView("/user/welcome")

        model.addAttribute(
            "suggestedEntries",
            hoursSuggestionCalculator.suggestHoursForAnAuthenticatedUser(
                user,
                client
            )
        )
        return "index"
    }

    @PostMapping("/enter")
    fun formPost(
        @ModelAttribute postedHours: PostedHours,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): RedirectView {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken

        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
            ?: return RedirectView("/user/welcome")

        hoursPoster.createTimeEntries(postedHours, user)
        return RedirectView("/")
    }
}
