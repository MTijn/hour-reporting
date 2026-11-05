package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.support.aCategory
import nl.martijnklene.hourreporting.support.aUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CategoryMapperTest {
    private val mapper = CategoryMapper()

    private val user =
        aUser(
            categories =
                listOf(
                    aCategory("Development", "DEV-1", 100),
                    aCategory("Support", "SUP-2", 200, default = true)
                )
        )

    @Test
    fun `maps a known category to its jira task id and key`() {
        assertEquals(100, mapper.mapCategoryToJiraTaskId("Development", user))
        assertEquals("DEV-1", mapper.mapCategoryToJiraKey("Development", user))
    }

    @Test
    fun `falls back to the default category for an unknown category`() {
        assertEquals(200, mapper.mapCategoryToJiraTaskId("Unknown", user))
        assertEquals("SUP-2", mapper.mapCategoryToJiraKey("Unknown", user))
    }

    @Test
    fun `falls back to the default category when no category is given`() {
        assertEquals(200, mapper.mapCategoryToJiraTaskId(null, user))
        assertEquals("SUP-2", mapper.mapCategoryToJiraKey(null, user))
    }

    @Test
    fun `returns null when there is no match and no default`() {
        val userWithoutDefault = aUser(categories = listOf(aCategory("Development", "DEV-1", 100)))

        assertNull(mapper.mapCategoryToJiraTaskId("Unknown", userWithoutDefault))
        assertNull(mapper.mapCategoryToJiraKey("Unknown", userWithoutDefault))
    }
}
