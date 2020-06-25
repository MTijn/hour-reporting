package nl.martijnklene.hourreporting.application.encryption

interface StringEncryption {
    fun encryptText(text: String): String
    fun decryptText(text: String): String
}
