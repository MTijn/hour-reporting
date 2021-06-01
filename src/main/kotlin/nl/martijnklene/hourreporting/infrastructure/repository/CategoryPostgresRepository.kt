package nl.martijnklene.hourreporting.infrastructure.repository

import nl.martijnklene.hourreporting.application.model.Category
import nl.martijnklene.hourreporting.application.repository.CategoryRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CategoryPostgresRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : CategoryRepository {
    @Throws(Exception::class)
    override fun save(category: Category) {
        val parameterSource = MapSqlParameterSource()
            .addValue("userId", category.userId)
            .addValue("outlookCategoryId", category.outlookTaskId)
            .addValue("clockifyProjectId", category.clockifyProjectId)
            .addValue("default", category.default)

        jdbcTemplate.update(
            "replace into category (user_id, outlook_category_id, clockify_project_id, \"default\") values " +
                    "(:userId, :outlookCategoryId, :clockifyProjectId, :default)",
            parameterSource
        )
    }

    override fun findCategoriesByUserId(userId: UUID): List<Category> {
        val parameterSource = MapSqlParameterSource().addValue("userId", userId)
        return jdbcTemplate.query(
            "select user_id, name, clockify_project_id, \"default\" from category where user_id = :userId",
            parameterSource
        ) { it, _ ->
            return@query Category(
                UUID.fromString(it.getString("user_id")),
                UUID.fromString(it.getString("outlook_category_id")),
                it.getString("clockify_project_id"),
                it.getBoolean("default")
            )
        }
    }

    override fun findCategoryByUserIdAndTask(userId: UUID, task: String): Category? {
        val parameterSource = MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("name", task)

        return jdbcTemplate.query(
            "select user_id, name, clockify_project_id, \"default\" from category where user_id = :userId and name = :name",
            parameterSource
        ) { it, _ ->
            return@query Category(
                UUID.fromString(it.getString("user_id")),
                UUID.fromString(it.getString("outlook_category_id")),
                it.getString("clockify_project_id"),
                it.getBoolean("default")
            )
        }.first()
    }

    override fun findDefaultCategoryForUser(userId: UUID): Category? {
        val parameterSource = MapSqlParameterSource()
            .addValue("userId", userId)

        return jdbcTemplate.query(
            "select user_id, name, clockify_project_id, \"default\" from category where user_id = :userId and name = :name and `default` = true",
            parameterSource
        ) { it, _ ->
            return@query Category(
                UUID.fromString(it.getString("user_id")),
                UUID.fromString(it.getString("outlook_category_id")),
                it.getString("clockify_project_id"),
                it.getBoolean("default")
            )
        }.first()
    }

    @Throws(Exception::class)
    override fun deleteCategoriesFromUserId(userId: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("userId", userId.toString())
        jdbcTemplate.update("delete from category where user_id = :userId", parameterSource)
    }
}
