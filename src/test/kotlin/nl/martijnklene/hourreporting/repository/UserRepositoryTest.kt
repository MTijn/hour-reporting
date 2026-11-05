package nl.martijnklene.hourreporting.repository

import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aCategory
import nl.martijnklene.hourreporting.support.aUser
import nl.martijnklene.hourreporting.support.anyObject
import nl.martijnklene.hourreporting.support.capture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.postgresql.util.PGobject
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import tools.jackson.module.kotlin.jacksonMapperBuilder
import java.sql.ResultSet

class UserRepositoryTest {
    private val jdbcTemplate = mock(NamedParameterJdbcTemplate::class.java)
    private val objectMapper = jacksonMapperBuilder().build()
    private val repository = UserRepository(jdbcTemplate, objectMapper)
    private val user = aUser(categories = listOf(aCategory("Development", "DEV-1", 100, default = true)))

    @Test
    fun `upserts the user as json`() {
        val parameters = ArgumentCaptor.forClass(SqlParameterSource::class.java)

        repository.save(user)

        verify(jdbcTemplate).update(
            org.mockito.Mockito.eq(
                "insert into \"user\" (id, data) values (:id, :data) on conflict (id) do update set data = :data"
            ),
            capture(parameters)
        )
        assertEquals(USER_ID, parameters.value.getValue("id"))
        val data = parameters.value.getValue("data") as PGobject
        assertEquals("json", data.type)
        assertEquals(user, objectMapper.readValue(data.value, User::class.java))
    }

    @Test
    fun `reads a user from its json column`() {
        val stored = PGobject()
        stored.type = "json"
        stored.value = objectMapper.writeValueAsString(user)
        val resultSet = mock(ResultSet::class.java)
        `when`(resultSet.getObject("data")).thenReturn(stored)
        `when`(jdbcTemplate.query(anyString(), anyObject<SqlParameterSource>(), anyObject<RowMapper<User>>()))
            .thenAnswer { invocation ->
                val rowMapper = invocation.getArgument<RowMapper<User>>(2)
                listOf(rowMapper.mapRow(resultSet, 0))
            }

        assertEquals(user, repository.findUserById(USER_ID))
    }

    @Test
    fun `returns null when the user does not exist`() {
        `when`(jdbcTemplate.query(anyString(), anyObject<SqlParameterSource>(), anyObject<RowMapper<User>>()))
            .thenReturn(emptyList())

        assertNull(repository.findUserById(USER_ID))
    }

    @Test
    fun `deletes the user by id`() {
        val parameters = ArgumentCaptor.forClass(SqlParameterSource::class.java)

        repository.deleteUserById(USER_ID)

        verify(jdbcTemplate).update(org.mockito.Mockito.eq("delete from \"user\" where id = :id"), capture(parameters))
        assertEquals(USER_ID.toString(), (parameters.value as MapSqlParameterSource).getValue("id"))
    }
}
