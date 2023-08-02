package nl.martijnklene.hourreporting.controllers

import com.microsoft.graph.models.OutlookCategory
import nl.martijnklene.hourreporting.controllers.response.PostedCategoryMapping
import nl.martijnklene.hourreporting.dto.Category
import nl.martijnklene.hourreporting.microsoft.service.CategoriesFetcher
import nl.martijnklene.hourreporting.repository.CategoryRepository
import nl.martijnklene.hourreporting.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
class CategoryController(
    private val userRepository: UserRepository,
    private val categoriesFetcher: CategoriesFetcher,
    private val categoryRepository: CategoryRepository
) {
    @GetMapping("/categories")
    fun mapCategoriesToJiraTasks(
        modelMap: ModelMap,
        @RegisteredOAuth2AuthorizedClient("graph") client: OAuth2AuthorizedClient
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
            ?: return "redirect:/"
        modelMap.addAttribute(
            "categories",
            categoriesFetcher.findCategoriesDefinedByTheUser(client)
                .map { it.toCategoryDto(categoryRepository.findConfiguredCategoriesForUser(user)) })
        return "categories"
    }

    @PostMapping("/categories")
    fun changeMapping(postedForm: PostedCategoryMapping): String {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))
            ?: return "redirect:/"

        postedForm.categories?.filter { it.value.isNotEmpty() }?.forEach {
            categoryRepository.save(
                nl.martijnklene.hourreporting.model.Category(
                user.id,
                UUID.fromString(it.key),
                it.value,
                postedForm.default == it.key
            ))
        }
        return "redirect:categories"
    }

    @GetMapping("/categories/ignored")
    fun configureIgnoredCategories(modelMap: ModelMap): Any {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = userRepository.findUserById(UUID.fromString(authentication.principal.attributes["oid"].toString()))

        return "ignore_categories"
    }

    @PostMapping("/categories/ignored")
    fun changeIgnoredMapping() {

    }

    fun OutlookCategory.toCategoryDto(userCategories: Collection<nl.martijnklene.hourreporting.model.Category>) =
        Category(
            id = UUID.fromString(id),
            displayName = displayName,
            isDefault = userCategories.firstOrNull { it.outlookTaskId == UUID.fromString(id) }?.default == true,
            jiraTicketId = userCategories.firstOrNull { it.outlookTaskId == UUID.fromString(id) }?.jiraProjectId
        )
}
