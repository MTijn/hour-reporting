package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.model.IgnoredCategory
import nl.martijnklene.hourreporting.support.USER_ID
import nl.martijnklene.hourreporting.support.aUser
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IgnoredCategoriesTest {
    private val ignoredCategories = IgnoredCategories()
    private val user = aUser(ignoredCategories = listOf(IgnoredCategory(USER_ID, "Lunch")))

    @Test
    fun `ignores a category the user has marked as ignored`() {
        assertTrue(ignoredCategories.shouldCategoryBeIgnored("Lunch", user))
    }

    @Test
    fun `does not ignore other categories`() {
        assertFalse(ignoredCategories.shouldCategoryBeIgnored("Development", user))
    }

    @Test
    fun `does not ignore a missing category`() {
        assertFalse(ignoredCategories.shouldCategoryBeIgnored(null, user))
    }
}
