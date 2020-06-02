package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.application.repository.UserRepository
import nl.martijnklene.hourreporting.infrastructure.external.outlook.CategoriesProvider
import nl.martijnklene.hourreporting.infrastructure.external.outlook.UserProvider
import nl.martijnklene.hourreporting.infrastructure.http.dto.UserDto
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class UserController(
    private val userRepository: UserRepository,
    private val userProvider: UserProvider,
    private val categoriesProvider: CategoriesProvider
) {
    @GetMapping("/user/welcome")
    fun welcomeUser(authentication: Authentication, modelMap: ModelMap): Any {
        val user = userProvider.findOutlookUser(authentication)
        if (userRepository.findOneUserById(UUID.fromString(user.id)) != null) {
            RedirectView("/")
        }
        modelMap.addAttribute("userName", user.displayName)
        modelMap.addAttribute("categories", categoriesProvider.findCategoriesForAuthenticatedUser(authentication))
        return "welcome"
    }

    @PostMapping("/user")
    fun createUser(authentication: Authentication, @ModelAttribute user: UserDto): Any {
        val key = user.apiKey
        return RedirectView("/")
    }
    @GetMapping("/user/delete/{userId}")
    fun deleteUser(@PathVariable userId: String): RedirectView {
        userRepository.deleteUserById(UUID.fromString(userId))
        return RedirectView("/logout")
    }
}
