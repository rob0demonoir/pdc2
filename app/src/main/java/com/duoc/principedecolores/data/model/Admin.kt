package com.duoc.principedecolores.data.model

data class Admin(
    val id: Int = 0,
    val username: String,
    val password: String // En producción usar hash, por ahora texto plano
)