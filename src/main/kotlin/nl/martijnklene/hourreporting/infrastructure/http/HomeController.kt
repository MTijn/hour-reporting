package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
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

@Controller
class HomeController(
    private var suggestionCalculator: HoursSuggestionCalculator,
    private var timeEntries: TimeEntries,
    private var timePoster: HoursPoster
) {
    @GetMapping("/")
    fun displayArticle(model: ModelMap, authentication: Authentication): String {
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
