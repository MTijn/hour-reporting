package nl.martijnklene.hourreporting.repository

import nl.martijnklene.hourreporting.model.Category
import nl.martijnklene.hourreporting.model.User
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

@Component
class CategoryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    private val rowMapper = RowMapper { resultSet: ResultSet, _: Int ->
        Category(
            id = UUID.fromString(resultSet.getString("id")),
            userId = UUID.fromString(resultSet.getString("user_id")),
            outlookTaskId = UUID.fromString(resultSet.getString("outlook_category_id")),
            jiraProjectId = resultSet.getString("jira_project_id"),
            default = resultSet.getBoolean("default")
        )
    }
    fun findConfiguredCategoriesForUser(user: User): Collection<Category> {
        return jdbcTemplate.query(
            "select * from category where user_id = :userId",
            mapOf("userId" to user.id.toString()),
            rowMapper
        )
    }
}
