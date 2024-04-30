package nl.martijnklene.hourreporting.repository

import nl.martijnklene.hourreporting.model.Category
import nl.martijnklene.hourreporting.model.IgnoredCategory
import nl.martijnklene.hourreporting.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

@Component
class CategoryRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    private val rowMapper = RowMapper { resultSet: ResultSet, _: Int ->
        Category(
            userId = UUID.fromString(resultSet.getString("user_id")),
            categoryName = resultSet.getString("category_name"),
            id = resultSet.getInt("jira_project_id"),
            jiraKey = resultSet.getString("jira_issue_key"),
            default = resultSet.getBoolean("default")
        )
    }

    private val ignoredCategoryMapper = RowMapper { resultSet: ResultSet, _: Int ->
        IgnoredCategory(
            userId = UUID.fromString(resultSet.getString("user_id")),
            name = resultSet.getString("name")
        )
    }
    fun findConfiguredCategoriesForUser(user: User): Collection<Category> {
        return jdbcTemplate.query(
            "select * from category where user_id = ?",
            rowMapper,
            user.id.toString()
        )
    }

    fun save(category: Category) {
        jdbcTemplate.update(
            "replace into `category` (`user_id`, `category_name`, `jira_project_id`, `jira_issue_key`, `default`) values (?, ?, ?, ?, ?)",
            category.userId.toString(),
            category.categoryName,
            category.id,
            category.jiraKey,
            category.default
        )
    }

    fun findIgnoredCategoriesForUser(user: User): Collection<IgnoredCategory> {
        return jdbcTemplate.query(
            "select * from ignored_category where user_id = ?",
            ignoredCategoryMapper,
            user.id.toString()
        )
    }

    fun saveIgnoredCategory(ignoredCategory: IgnoredCategory) {
        jdbcTemplate.update(
            "replace into `ignored_category` (`user_id`, `name`) values (?, ?)",
            ignoredCategory.userId.toString(),
            ignoredCategory.name
        )
    }

    fun deleteIgnoredCategories(user: User) {
        jdbcTemplate.update(
            "delete from `ignored_category` where `user_id` = ?",
            user.id.toString()
        )
    }
}
