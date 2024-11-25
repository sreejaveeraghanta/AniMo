package AniMo.com

import java.security.MessageDigest

object Util {
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())

        val stringBuilder = StringBuilder()
        for (byte in hashedBytes) {
            stringBuilder.append(String.format("%02x", byte))
        }

        return stringBuilder.toString()
    }
}