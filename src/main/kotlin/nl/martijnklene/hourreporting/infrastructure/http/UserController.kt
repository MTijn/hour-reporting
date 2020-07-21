package nl.martijnklene.hourreporting.infrastructure.http

import nl.martijnklene.hourreporting.application.encryption.StringEncryption
import nl.martijnklene.hourreporting.application.model.User
import nl.martijnklene.hourreporting.application.repository.UserRepository
import nl.martijnklene.hourreporting.infrastructure.external.clockify.Projects
import nl.martijnklene.hourreporting.infrastructure.external.outlook.CategoriesProvider
import nl.martijnklene.hourreporting.infrastructure.external.outlook.UserProvider
import nl.martijnklene.hourreporting.infrastructure.http.dto.CategoriesDto
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
    private val categoriesProvider: CategoriesProvider,
    private val encryption: StringEncryption,
    private val clockifyProjects: Projects
) {
    @GetMapping("/user")
    fun viewUser(authentication: Authentication, modelMap: ModelMap): Any {
        modelMap.addAttribute("categories", categoriesProvider.findCategoriesForAuthenticatedUser(authentication))
        modelMap.addAttribute("projects", clockifyProjects.projectsForUser())
        return "user"
    }

    @GetMapping("/user/welcome")
    fun welcomeUser(authentication: Authentication, modelMap: ModelMap): Any {
        val user = userProvider.findOutlookUser(authentication)
        if (userRepository.findOneUserById(UUID.fromString(user.id)) != null) {
            RedirectView("/")
        }
        modelMap.addAttribute("userName", user.displayName)
        return "welcome"
    }

    @PostMapping("/user")
    fun createUser(authentication: Authentication, @ModelAttribute user: UserDto): Any {
        val outlookUser = userProvider.findOutlookUser(authentication)
        userRepository.save(User(
            UUID.fromString(outlookUser.id),
            outlookUser.displayName,
            emptyList(),
            emptyList(),
            encryption.encryptText(user.apiKey)
        ))
        return RedirectView("/")
    }

    @PostMapping("/user/categories")
    fun storeCategories(@ModelAttribute categories: CategoriesDto): Any {
        return RedirectView("/user")
    }

    @GetMapping("/user/delete/{userId}")
    fun deleteUser(@PathVariable userId: String): RedirectView {
        userRepository.deleteUserById(UUID.fromString(userId))
        return RedirectView("/logout")
    }
}
