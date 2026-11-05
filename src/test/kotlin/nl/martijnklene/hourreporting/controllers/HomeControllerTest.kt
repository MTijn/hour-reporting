package nl.martijnklene.hourreporting.controllers

import nl.martijnklene.hourreporting.controllers.dto.SuggestedTimeEntry
import nl.martijnklene.hourreporting.controllers.response.PostedHours
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.service.HoursPoster
import nl.martijnklene.hourreporting.service.HoursSuggestionCalculator
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.support.anAuthorizedClient
import nl.martijnklene.hourreporting.support.authenticateAs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.ModelMap
import org.springframework.web.servlet.view.RedirectView
import java.time.Duration
import java.time.LocalDate

class HomeControllerTest {
    private val userRepository = mock(UserRepository::class.java)
    private val hoursPoster = mock(HoursPoster::class.java)
    private val hoursSuggestionCalculator = mock(HoursSuggestionCalculator::class.java)
    private val controller = HomeController(userRepository, hoursPoster, hoursSuggestionCalculator)
    private val client = anAuthorizedClient()

    @BeforeEach
    fun setUp() {
        authenticateAs()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `sends unknown users to the welcome page`() {
        val view = controller.homeScreen(ModelMap(), client)

        assertEquals("/user/welcome", assertInstanceOf(RedirectView::class.java, view).url)
        verifyNoInteractions(hoursSuggestionCalculator)
    }

    @Test
    fun `shows the suggested entries for a known user`() {
        val user = aUser()
        val suggestions = listOf(SuggestedTimeEntry(Duration.ofHours(8), 1, "DEV-1", "Meeting", LocalDate.of(2026, 10, 1)))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)
        `when`(hoursSuggestionCalculator.suggestHoursForAnAuthenticatedUser(user, client)).thenReturn(suggestions)
        val model = ModelMap()

        val view = controller.homeScreen(model, client)

        assertEquals("index", view)
        assertEquals(user, model["user"])
        assertEquals(suggestions, model["suggestedEntries"])
    }

    @Test
    fun `posts the submitted hours for a known user`() {
        val user = aUser()
        val postedHours = PostedHours(hashMapOf("2026-10-01" to hashMapOf("hours" to "60")))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)

        val view = controller.formPost(postedHours, client)

        assertEquals("/", view.url)
        verify(hoursPoster).createTimeEntries(postedHours, user)
    }

    @Test
    fun `does not post hours for unknown users`() {
        val view = controller.formPost(PostedHours(null), client)

        assertEquals("/user/welcome", view.url)
        verifyNoInteractions(hoursPoster)
    }
}
