package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.clockify.service.TimeEntriesService
import nl.martijnklene.hourreporting.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class HomeController(
    private val timeEntriesService: TimeEntriesService,
    private val userRepository: UserRepository
) {
    @GetMapping("/home")
    fun homeScreen(model: ModelMap): Any {
        val user = userRepository.findUserById(UUID.randomUUID()) ?: return "index"
        timeEntriesService.lastClockifyTimeEntry(user.clockifyApiKey)?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggestionCalculator.suggestHoursForAnAuthenticatedUser(
                authentication,
                user.clockifyApiKey
            )
        )
        return "index"
    }
}
