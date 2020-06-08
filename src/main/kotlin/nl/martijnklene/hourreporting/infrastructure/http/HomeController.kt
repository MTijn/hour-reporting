package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.application.repository.UserRepository
import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
import nl.martijnklene.hourreporting.infrastructure.external.outlook.UserProvider
import nl.martijnklene.hourreporting.infrastructure.http.dto.FormEntity
import nl.martijnklene.hourreporting.infrastructure.service.HoursPoster
import nl.martijnklene.hourreporting.infrastructure.service.HoursSuggestionCalculator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class HomeController(
    private val suggestionCalculator: HoursSuggestionCalculator,
    private val timeEntries: TimeEntries,
    private val timePoster: HoursPoster,
    private val userProvider: UserProvider,
    private val userRepository: UserRepository
) {
    @GetMapping("/")
    fun displayArticle(model: ModelMap, authentication: Authentication): Any {
        val user = userProvider.findOutlookUser(authentication)
        if (userRepository.findOneUserById(UUID.fromString(user.id)) == null) {
            return RedirectView("/user/welcome")
        }
        timeEntries.lastClockifyTimeEntry()?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggestionCalculator.suggestHoursForAnAuthenticatedUser(
                authentication
            )
        )
        return "index"
    }

    @PostMapping("/enter")
    fun formPost(@ModelAttribute formEntity: FormEntity): RedirectView {
        timePoster.createTimeEntries(formEntity)
        return RedirectView("/")
    }
}
