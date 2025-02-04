package nl.martijnklene.hourreporting.controllers

import com.microsoft.graph.models.OutlookCategory
import nl.martijnklene.hourreporting.controllers.dto.Category
import nl.martijnklene.hourreporting.controllers.dto.IgnoredCategory
import nl.martijnklene.hourreporting.controllers.response.PostedCategoryMapping
import nl.martijnklene.hourreporting.controllers.response.PostedIgnoredCategories
import nl.martijnklene.hourreporting.jira.service.JiraUserFetcher
import nl.martijnklene.hourreporting.microsoft.service.CategoriesFetcher
import nl.martijnklene.hourreporting.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

@Controller
class CategoryController(
    private val userRepository: UserRepository,
    private val categoriesFetcher: CategoriesFetcher,
    private val jiraIssueFetcher: JiraUserFetcher
) {
    @GetMapping("/categories")
    fun mapCategoriesToJiraTasks(
        modelMap: ModelMap,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user =
            userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
                ?: return "redirect:/"
        modelMap.addAttribute("user", user)
        modelMap.addAttribute(
            "categories",
            categoriesFetcher
                .findCategoriesDefinedByTheUser(client)
                .map { it.toCategoryDto(user.categories) }
        )
        return "categories"
    }

    @PostMapping("/categories")
    fun changeMapping(postedForm: PostedCategoryMapping): String {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user =
            userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
                ?: return "redirect:/"

        val categories = mutableListOf<nl.martijnklene.hourreporting.model.Category>()
        postedForm.categories?.filter { it.value.isNotEmpty() }?.forEach {
            val jiraIssue = jiraIssueFetcher.findJiraIssuesByIssueKey(it.value, user.jiraUserName, user.jiraApiKey)
            categories.add(
                nl.martijnklene.hourreporting.model.Category(
                    userId = user.id,
                    id = jiraIssue.id,
                    jiraKey = jiraIssue.key,
                    categoryName = it.key,
                    default = postedForm.default == it.key
                )
            )
        }
        user.categories = categories
        userRepository.save(user)
        return "redirect:categories"
    }

    @GetMapping("/categories/ignored")
    fun configureIgnoredCategories(
        modelMap: ModelMap,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user =
            userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
                ?: return "redirect:/"
        modelMap.addAttribute("user", user)
        modelMap.addAttribute(
            "categories",
            categoriesFetcher
                .findCategoriesDefinedByTheUser(client)
                .map { it.toIgnoredCategory(user.ignoredCategories) }
        )
        return "ignore_categories"
    }

    @PostMapping("/categories/ignored")
    fun changeIgnoredMapping(ignoredCategories: PostedIgnoredCategories): String {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user =
            userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
                ?: return "redirect:/"

        val ignoredCategoriesToUpdate = mutableListOf<nl.martijnklene.hourreporting.model.IgnoredCategory>()
        if (!ignoredCategories.categories.isNullOrEmpty()) {
            ignoredCategories.categories?.filter { it.isNotEmpty() }?.forEach {
                ignoredCategoriesToUpdate.add(
                    nl.martijnklene.hourreporting.model.IgnoredCategory(
                        userId = user.id,
                        name = it
                    )
                )
            }
        }
        user.ignoredCategories = ignoredCategoriesToUpdate
        userRepository.save(user)
        return "redirect:/categories/ignored"
    }

    fun OutlookCategory.toCategoryDto(userCategories: Collection<nl.martijnklene.hourreporting.model.Category>) =
        Category(
            id = UUID.fromString(id),
            displayName = displayName,
            isDefault = userCategories.firstOrNull { it.categoryName == displayName }?.default == true,
            jiraTicketId = userCategories.firstOrNull { it.categoryName == displayName }?.id,
            jiraKey = userCategories.firstOrNull { it.categoryName == displayName }?.jiraKey
        )

    fun OutlookCategory.toIgnoredCategory(userCategories: Collection<nl.martijnklene.hourreporting.model.IgnoredCategory>) =
        IgnoredCategory(
            id = UUID.fromString(id),
            displayName = displayName,
            userCategories.firstOrNull { it.name == displayName } != null
        )
}
