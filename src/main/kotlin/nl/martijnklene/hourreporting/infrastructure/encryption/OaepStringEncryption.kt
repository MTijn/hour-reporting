package nl.martijnklene.hourreporting.infrastructure.encryption

import nl.martijnklene.hourreporting.application.encryption.StringEncryption
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Component
class OaepStringEncryption(
    @Value("\${encryption.key.private}") private val privateKey: String,
    @Value("\${encryption.key.public}") private val publicKey: String
): StringEncryption {
    override fun encryptText(text: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, createPublicKeyFromString())
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8)))
    }

    override fun decryptText(text: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.DECRYPT_MODE, createPrivateKeyFromString())
        return String(cipher.doFinal(Base64.getDecoder().decode(text)))
    }

    private fun createPrivateKeyFromString(): PrivateKey {
        val path: Path = Paths.get(privateKey)
        var privateKey = String(Files.readAllBytes(path))
        privateKey = privateKey.replace("\\n".toRegex(), "")
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
        val bytes = Base64.getDecoder().decode(privateKey)

        val kf = KeyFactory.getInstance("RSA")

        val keySpecPKCS8 = PKCS8EncodedKeySpec(bytes)
        return kf.generatePrivate(keySpecPKCS8)

    }

    private fun createPublicKeyFromString(): RSAPublicKey {
        val path: Path = Paths.get(publicKey)
        var publicKey = String(Files.readAllBytes(path))
        publicKey = publicKey.replace("\\n".toRegex(), "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")

        val kf = KeyFactory.getInstance("RSA")
        val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
        return kf.generatePublic(keySpecX509) as RSAPublicKey
    }
}
