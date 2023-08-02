package nl.martijnklene.hourreporting.repository

import nl.martijnklene.hourreporting.model.Category
import nl.martijnklene.hourreporting.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
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
            outlookTaskId = UUID.fromString(resultSet.getString("outlook_category_id")),
            jiraProjectId = resultSet.getString("jira_project_id"),
            default = resultSet.getBoolean("default")
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
            "replace into `category` (`user_id`, `outlook_category_id`, `jira_project_id`, `default`) values (?, ?, ?, ?)",
            category.userId.toString(),
            category.outlookTaskId.toString(),
            category.jiraProjectId,
            category.default
        )
    }
}
