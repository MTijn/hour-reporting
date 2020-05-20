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
            .addValue("id", category.id.toString())
            .addValue("userId", category.userId.toString())
            .addValue("name", category.name)
            .addValue("clockifyProjectId", category.clockifyProjectId)
            .addValue("default", category.default)

        jdbcTemplate.update(
            "insert into category (`id`, `user_id`, `name`, `clockify_project_id`, `default`) values " +
                    "(:id, :userId, :name, :clockifyProjectId, :default)",
            parameterSource
        )
    }

    override fun findCategoriesByUserId(userId: UUID): List<Category> {
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    override fun deleteCategoryById(id: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", id.toString())
        jdbcTemplate.update("delete from category where id = :id", parameterSource)

    }

    @Throws(Exception::class)
    override fun deleteCategoriesFromUserId(userId: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("userId", userId.toString())
        jdbcTemplate.update("delete from category where user_id = :userId", parameterSource)
    }
}
