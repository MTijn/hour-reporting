package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.encryption.StringEncryption
import nl.martijnklene.hourreporting.jira.service.JiraUserFetcher
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.support.authenticateAs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.ModelMap

class UserControllerTest {
    private val userRepository = mock(UserRepository::class.java)
    private val controller =
        UserController(userRepository, mock(StringEncryption::class.java), mock(JiraUserFetcher::class.java))

    @BeforeEach
    fun setUp() {
        authenticateAs(name = "Jane Doe")
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `welcomes a user that has not registered yet`() {
        val model = ModelMap()

        assertEquals("welcome", controller.welcomeUser(model))
        assertEquals("Jane Doe", model["userName"])
    }

    @Test
    fun `sends registered users home instead of welcoming them`() {
        `when`(userRepository.findUserById(USER_ID)).thenReturn(aUser())
        val model = ModelMap()

        assertEquals("redirect:/", controller.welcomeUser(model))
        assertFalse(model.containsAttribute("userName"))
    }

    @Test
    fun `shows the user page`() {
        val model = ModelMap()

        assertEquals("user", controller.user(model))
        assertEquals("Jane Doe", model["userName"])
    }

    @Test
    fun `deletes the user and logs out`() {
        val view = controller.deleteUser(USER_ID.toString())

        assertEquals("/logout", view.url)
        verify(userRepository).deleteUserById(USER_ID)
    }
}
