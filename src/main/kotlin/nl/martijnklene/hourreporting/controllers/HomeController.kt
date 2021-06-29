package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.service.HoursSuggestionCalculator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class HomeController(
    private val timeEntriesService: TimeEntriesService,
    private val userRepository: UserRepository,
    private val suggesterService: HoursSuggestionCalculator,
) {
    @GetMapping("/")
    fun homeScreen(model: ModelMap, @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
        timeEntriesService.lastClockifyTimeEntry(user!!.clockifyApiKey)?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggesterService.suggestHoursForAnAuthenticatedUser(
                user.clockifyApiKey,
                client
            )
        )
        return "index"
    }
}
