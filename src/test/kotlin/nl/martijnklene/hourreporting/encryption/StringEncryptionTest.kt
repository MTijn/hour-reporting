package nl.martijnklene.hourreporting.encryption

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.security.KeyPairGenerator
import java.util.*
import javax.crypto.BadPaddingException

class StringEncryptionTest {
    @TempDir
    lateinit var keyDirectory: Path

    private lateinit var encryption: StringEncryption

    @BeforeEach
    fun setUp() {
        encryption = createEncryption(keyDirectory, "first")
    }

    @Test
    fun `decrypts what it encrypted`() {
        val encrypted = encryption.encryptText("my-secret-api-key")

        assertNotEquals("my-secret-api-key", encrypted)
        assertEquals("my-secret-api-key", encryption.decryptText(encrypted))
    }

    @Test
    fun `round trips non ascii text`() {
        assertEquals("sleutel-ëéü-✓", encryption.decryptText(encryption.encryptText("sleutel-ëéü-✓")))
    }

    @Test
    fun `produces a different cipher text for every encryption`() {
        assertNotEquals(encryption.encryptText("same"), encryption.encryptText("same"))
    }

    @Test
    fun `cannot decrypt text encrypted with another key pair`() {
        val otherEncryption = createEncryption(keyDirectory, "second")

        assertThrows<BadPaddingException> {
            encryption.decryptText(otherEncryption.encryptText("my-secret-api-key"))
        }
    }

    @Test
    fun `fails when the key files do not exist`() {
        val missing = StringEncryption("does/not/exist.pem", "does/not/exist.pem")

        assertThrows<NoSuchFileException> { missing.encryptText("text") }
    }

    private fun createEncryption(
        directory: Path,
        name: String
    ): StringEncryption {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val keyPair = generator.generateKeyPair()
        val privateKeyFile = directory.resolve("$name-private.pem")
        val publicKeyFile = directory.resolve("$name-public.pem")
        Files.writeString(privateKeyFile, toPem("PRIVATE KEY", keyPair.private.encoded))
        Files.writeString(publicKeyFile, toPem("PUBLIC KEY", keyPair.public.encoded))
        return StringEncryption(privateKeyFile.toString(), publicKeyFile.toString())
    }

    private fun toPem(
        type: String,
        encoded: ByteArray
    ): String {
        val body = Base64.getMimeEncoder(64, "\n".toByteArray()).encodeToString(encoded)
        return "-----BEGIN $type-----\n$body\n-----END $type-----\n"
    }
}
