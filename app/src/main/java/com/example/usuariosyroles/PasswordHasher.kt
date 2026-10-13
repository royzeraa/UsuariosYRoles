package com.example.comercializadoraangel

import java.security.MessageDigest

object PasswordHasher {
    // Hash básico para el proyecto académico. Para producción usa PBKDF2, scrypt o Argon2id con salt.
    fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
