package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.infrastructure.external.clockify.TimeEntries
import nl.martijnklene.hourreporting.infrastructure.service.HoursSuggestionCalculator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate

@Controller
class HomeController(
    private var suggestionCalculator: HoursSuggestionCalculator,
    private var timeEntries: TimeEntries
) {
    @GetMapping(value = ["/"])
    fun displayArticle(model: ModelMap, authentication: Authentication): String {
        timeEntries.lastClockifyTimeEntry()?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggestionCalculator.suggestHoursForARangeOfDays(
                listOf(
                    LocalDate.parse("2020-04-13"),
                    LocalDate.parse("2020-04-14"),
                    LocalDate.parse("2020-04-15"),
                    LocalDate.parse("2020-04-16"),
                    LocalDate.parse("2020-04-17")
                ),
                authentication
            )
        )
        return "index"
    }

    @PostMapping(value = ["/enter"])
    fun addTimeEntries(@RequestBody postedItems: List<Any>): String {
        return ""
    }
}
