package com.duoc.principedecolores.utils

import com.duoc.principedecolores.data.model.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    // Guardamos el usuario aquí
    private val _currentClient = MutableStateFlow<Cliente?>(null)
    val currentClient = _currentClient.asStateFlow()

    fun login(cliente: Cliente) {
        _currentClient.value = cliente
    }

    fun logout() {
        _currentClient.value = null
    }

    // Helper para saber si hay alguien logueado rápido
    fun isLoggedIn(): Boolean {
        return _currentClient.value != null
    }

    // Helper para obtener el ID (o lanzar error si no hay nadie)
    fun getClientId(): Int {
        return _currentClient.value?.id ?: throw Exception("Usuario no logueado")
    }
}