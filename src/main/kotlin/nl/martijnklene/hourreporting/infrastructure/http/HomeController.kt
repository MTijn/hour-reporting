package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.infrastructure.external.outlook.Calendar
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDate

@Controller
class HomeController(private var calendar: Calendar) {
    @GetMapping(value = ["/"])
    fun displayArticle(model: MutableMap<String, Any>): ModelAndView {
        val page = calendar.getEventsForADay(LocalDate.now())
        return ModelAndView("index", model)
    }
}
