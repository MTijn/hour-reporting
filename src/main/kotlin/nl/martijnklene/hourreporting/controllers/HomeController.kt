package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.service.DatesSuggesterService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class HomeController(
    private val timeEntriesService: TimeEntriesService,
    private val userRepository: UserRepository,
    private val suggesterService: DatesSuggesterService
) {
    @GetMapping("/")
    fun homeScreen(model: ModelMap): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
        timeEntriesService.lastClockifyTimeEntry(user!!.clockifyApiKey)?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggesterService.suggestDaysForTheAuthenticatedUser(
                user.clockifyApiKey
            )
        )
        return "index"
    }
}
