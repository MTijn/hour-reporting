package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.infrastructure.external.clockify.Clockify
import nl.martijnklene.hourreporting.infrastructure.external.outlook.Calendar
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController(private var calendar: Calendar, private var clockify: Clockify) {
    @GetMapping(value = ["/"])
    fun displayArticle(model: MutableMap<String, Any>): ModelAndView {
//        val page = calendar.getEventsForADay(LocalDate.now())
        val items = clockify.lastClockifyTimeEntry()
        return ModelAndView("index", model)
    }
}
