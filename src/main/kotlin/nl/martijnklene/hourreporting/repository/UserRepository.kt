package nl.martijnklene.hourreporting.repository

import nl.martijnklene.hourreporting.model.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    @Throws(Exception::class)
    fun save(user: User) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", user.id.toString())
            .addValue("name", user.name)
            .addValue("api_key", user.apiKey)
            .addValue("jira_user_name", user.jiraUserName)

        jdbcTemplate.update(
            "insert into `user` (id, name, jira_api_key, jira_user_name) values (:id, :name, :api_key, :jira_user_name)",
            parameterSource
        )
    }

    @Throws(Exception::class)
    fun findUserById(id: UUID): User? {
        val parameterSource = MapSqlParameterSource().addValue("id", id.toString())
        return jdbcTemplate.query("select id, name, jira_api_key, jira_user_name from `user` where id = :id", parameterSource) {
                resultSet, _ ->
            return@query User(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                emptyList(),
                emptyList(),
                resultSet.getString("jira_api_key"),
                resultSet.getString("jira_user_name")
            )
        }.firstOrNull()
    }

    @Throws(Exception::class)
    fun deleteUserById(id: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", id.toString())
        jdbcTemplate.update("delete from `user` where id = :id", parameterSource)
    }
}
