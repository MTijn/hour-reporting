package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.application.model.User
import nl.martijnklene.hourreporting.application.repository.UserRepository
import nl.martijnklene.hourreporting.infrastructure.external.outlook.OutlookUserFinder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class UserController(
    private val userRepository: UserRepository,
    private val outlookUserFinder: OutlookUserFinder
) {
    @GetMapping("/user/welcome")
    fun welcomeUser(authentication: Authentication) {

    }

    @PostMapping("/user")
    fun createUser(authentication: Authentication) {
        outlookUserFinder.findOutlookUser(authentication)
        userRepository.save(User(
            UUID.fromString(outlookUserFinder.findOutlookUser(authentication).id),
            "name",
            emptyList(),
            ""
        ))
    }
    @GetMapping("/user/delete/{userId}")
    fun deleteUser(@PathVariable userId: String): RedirectView {
        userRepository.deleteUserById(UUID.fromString(userId))
        return RedirectView("/logout")
    }
}
