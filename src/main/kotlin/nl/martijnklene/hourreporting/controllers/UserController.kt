package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.controllers.response.UserDto
import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.microsoft.service.UserPhotoFetcher
import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
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
    private val encryption: StringEncryption,
    private val userPhotoFetcher: UserPhotoFetcher
) {
    @GetMapping("/user/welcome")
    fun welcomeUser(modelMap: ModelMap): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
        if (user != null) {
            return "redirect:/"
        }
        modelMap.addAttribute("userName", authentication.name)
        return "welcome"
    }

    @PostMapping("/user")
    fun createUser(
        @ModelAttribute user: UserDto,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken

        val userImage = userPhotoFetcher.fetchPhotoFromLoggedInUser(client)
        userRepository.save(
            User(
                UUID.fromString(authentication.principal.attributes["oid"].toString()),
                authentication.name,
                emptyList(),
                emptyList(),
                encryption.encryptText(user.apiKey),
                user.jiraUserName,
                userImage
            )
        )
        return RedirectView("/")
    }

    @GetMapping("/user/delete/{userId}")
    fun deleteUser(
        @PathVariable userId: String
    ): RedirectView {
        userRepository.deleteUserById(UUID.fromString(userId))
        return RedirectView("/logout")
    }
}
