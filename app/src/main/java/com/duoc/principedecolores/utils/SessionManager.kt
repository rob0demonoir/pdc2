package com.duoc.principedecolores.utils

import com.duoc.principedecolores.data.model.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private val _currentClient = MutableStateFlow<Cliente?>(null)
    val currentClient = _currentClient.asStateFlow()

    fun login(cliente: Cliente) {
        _currentClient.value = cliente
    }

    fun logout() {
        _currentClient.value = null
    }

    fun isLoggedIn(): Boolean {
        return _currentClient.value != null
    }

    fun getClientId(): Int {
        return _currentClient.value?.id ?: throw Exception("Usuario no logueado")
    }
}