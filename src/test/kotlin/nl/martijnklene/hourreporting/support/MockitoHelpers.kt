package nl.martijnklene.hourreporting.support

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Mockito matchers return null, which Kotlin rejects for non-null parameters.
 * These helpers register the matcher and hand back an unchecked null that Kotlin does not null-check.
 */
@Suppress("UNCHECKED_CAST")
fun <T> anyObject(): T {
    Mockito.any<T>()
    return null as T
}

@Suppress("UNCHECKED_CAST")
fun <T> capture(captor: ArgumentCaptor<T>): T {
    captor.capture()
    return null as T
}
