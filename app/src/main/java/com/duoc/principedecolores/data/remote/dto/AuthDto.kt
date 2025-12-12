package com.duoc.principedecolores.data.remote.dto

// Representa el cuerpo de la petición para el login
data class LoginRequest(
    val username: String,
    val password: String
)

// Aunque la API devuelve solo un String, es buena práctica prepararse
// para una respuesta más compleja, como un token.
// Por ahora, no necesitaremos una clase de respuesta si solo es un String.