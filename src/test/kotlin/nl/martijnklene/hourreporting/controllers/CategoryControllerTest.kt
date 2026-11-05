package nl.martijnklene.hourreporting.controllers

import com.microsoft.graph.models.OutlookCategory
import nl.martijnklene.hourreporting.controllers.dto.Category
import nl.martijnklene.hourreporting.controllers.dto.IgnoredCategory
import nl.martijnklene.hourreporting.controllers.response.PostedCategoryMapping
import nl.martijnklene.hourreporting.controllers.response.PostedIgnoredCategories
import nl.martijnklene.hourreporting.jira.dto.Issue
import nl.martijnklene.hourreporting.jira.service.JiraUserFetcher
import nl.martijnklene.hourreporting.microsoft.service.CategoriesFetcher
import nl.martijnklene.hourreporting.model.IgnoredCategory as StoredIgnoredCategory
import nl.martijnklene.hourreporting.repository.UserRepository
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aCategory
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.support.anAuthorizedClient
import nl.martijnklene.hourreporting.support.authenticateAs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.ModelMap
import java.util.*

class CategoryControllerTest {
    private val userRepository = mock(UserRepository::class.java)
    private val categoriesFetcher = mock(CategoriesFetcher::class.java)
    private val jiraUserFetcher = mock(JiraUserFetcher::class.java)
    private val controller = CategoryController(userRepository, categoriesFetcher, jiraUserFetcher)
    private val client = anAuthorizedClient()

    private val developmentId = UUID.fromString("0f8fad5b-d9cb-469f-a165-70867728950e")
    private val lunchId = UUID.fromString("7c9e6679-7425-40de-944b-e07fc1f90ae7")

    @BeforeEach
    fun setUp() {
        authenticateAs()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `redirects home when the user is unknown`() {
        assertEquals("redirect:/", controller.mapCategoriesToJiraTasks(ModelMap(), client))
        assertEquals("redirect:/", controller.changeMapping(PostedCategoryMapping(mapOf("a" to "b"), null)))
        assertEquals("redirect:/", controller.configureIgnoredCategories(ModelMap(), client))
        assertEquals("redirect:/", controller.changeIgnoredMapping(PostedIgnoredCategories(listOf("a"))))
        verifyNoInteractions(categoriesFetcher, jiraUserFetcher)
    }

    @Test
    fun `shows the outlook categories with their jira mapping`() {
        val user = aUser(categories = listOf(aCategory("Development", "DEV-1", 100, default = true)))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)
        `when`(categoriesFetcher.findCategoriesDefinedByTheUser(client))
            .thenReturn(listOf(anOutlookCategory(developmentId, "Development"), anOutlookCategory(lunchId, "Lunch")))
        val model = ModelMap()

        val view = controller.mapCategoriesToJiraTasks(model, client)

        assertEquals("categories", view)
        assertEquals(user, model["user"])
        assertEquals(
            listOf(
                Category(developmentId, "Development", true, 100, "DEV-1"),
                Category(lunchId, "Lunch", false, null, null)
            ),
            model["categories"]
        )
    }

    @Test
    fun `stores the posted category mapping and skips empty mappings`() {
        val user = aUser()
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)
        `when`(jiraUserFetcher.findJiraIssuesByIssueKey("DEV-1", user.jiraUserName, user.jiraApiKey))
            .thenReturn(Issue("", 100, "", "DEV-1", LinkedHashMap()))
        `when`(jiraUserFetcher.findJiraIssuesByIssueKey("SUP-2", user.jiraUserName, user.jiraApiKey))
            .thenReturn(Issue("", 200, "", "SUP-2", LinkedHashMap()))

        val view =
            controller.changeMapping(
                PostedCategoryMapping(
                    linkedMapOf("Development" to "DEV-1", "Support" to "SUP-2", "Lunch" to ""),
                    "Support"
                )
            )

        assertEquals("redirect:categories", view)
        assertEquals(
            listOf(
                aCategory("Development", "DEV-1", 100, default = false),
                aCategory("Support", "SUP-2", 200, default = true)
            ),
            user.categories
        )
        verify(userRepository).save(user)
    }

    @Test
    fun `clears the mapping when nothing is posted`() {
        val user = aUser(categories = listOf(aCategory("Development", "DEV-1", 100)))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)

        controller.changeMapping(PostedCategoryMapping(null, null))

        assertEquals(emptyList<Any>(), user.categories)
        verify(userRepository).save(user)
    }

    @Test
    fun `shows which outlook categories are ignored`() {
        val user = aUser(ignoredCategories = listOf(StoredIgnoredCategory(USER_ID, "Lunch")))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)
        `when`(categoriesFetcher.findCategoriesDefinedByTheUser(client))
            .thenReturn(listOf(anOutlookCategory(developmentId, "Development"), anOutlookCategory(lunchId, "Lunch")))
        val model = ModelMap()

        val view = controller.configureIgnoredCategories(model, client)

        assertEquals("ignore_categories", view)
        assertEquals(
            listOf(
                IgnoredCategory(developmentId, "Development", false),
                IgnoredCategory(lunchId, "Lunch", true)
            ),
            model["categories"]
        )
    }

    @Test
    fun `stores the posted ignored categories and skips empty names`() {
        val user = aUser()
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)

        val view = controller.changeIgnoredMapping(PostedIgnoredCategories(listOf("Lunch", "", "Private")))

        assertEquals("redirect:/categories/ignored", view)
        assertEquals(
            listOf(
                StoredIgnoredCategory(USER_ID, "Lunch"),
                StoredIgnoredCategory(USER_ID, "Private")
            ),
            user.ignoredCategories
        )
        verify(userRepository).save(user)
    }

    @Test
    fun `clears the ignored categories when nothing is posted`() {
        val user = aUser(ignoredCategories = listOf(StoredIgnoredCategory(USER_ID, "Lunch")))
        `when`(userRepository.findUserById(USER_ID)).thenReturn(user)

        controller.changeIgnoredMapping(PostedIgnoredCategories(null))

        assertEquals(emptyList<Any>(), user.ignoredCategories)
        verify(userRepository).save(user)
    }

    private fun anOutlookCategory(
        id: UUID,
        name: String
    ): OutlookCategory {
        val category = OutlookCategory()
        category.id = id.toString()
        category.displayName = name
        return category
    }
}
