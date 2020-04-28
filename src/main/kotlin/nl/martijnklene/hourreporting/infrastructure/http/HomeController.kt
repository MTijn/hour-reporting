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
import java.time.LocalDate

@Controller
class HomeController(
    private var suggestionCalculator: HoursSuggestionCalculator,
    private var timeEntries: TimeEntries,
    private var timePoster: HoursPoster
) {
    @GetMapping(value = ["/"])
    fun displayArticle(model: ModelMap, authentication: Authentication): String {
        timeEntries.lastClockifyTimeEntry()?.let { model.addAttribute("lastTimeEntry", it) }
        model.addAttribute(
            "suggestedEntries",
            suggestionCalculator.suggestHoursForARangeOfDays(
                listOf(
                    LocalDate.parse("2020-04-01"),
                    LocalDate.parse("2020-04-02"),
                    LocalDate.parse("2020-04-03"),
                    LocalDate.parse("2020-04-06"),
                    LocalDate.parse("2020-04-07"),
                    LocalDate.parse("2020-04-08"),
                    LocalDate.parse("2020-04-09"),
                    LocalDate.parse("2020-04-10"),
                    LocalDate.parse("2020-04-20"),
                    LocalDate.parse("2020-04-21"),
                    LocalDate.parse("2020-04-22"),
                    LocalDate.parse("2020-04-23"),
                    LocalDate.parse("2020-04-24"),
                    LocalDate.parse("2020-04-27"),
                    LocalDate.parse("2020-04-28")
                ),
                authentication
            )
        )
        return "index"
    }

    @PostMapping("/enter")
    fun formPost(@ModelAttribute formEntity: FormEntity): String {
        timePoster.createTimeEntries(formEntity)
        return "index"
    }
}
