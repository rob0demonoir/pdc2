package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.model.Cliente
import com.duoc.principedecolores.data.repository.ClienteRepository
import com.duoc.principedecolores.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Definimos el estado de la UI para esta pantalla
data class LoginClientUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val clienteLogueado: Cliente? = null
)

class LoginClienteViewModel(private val clienteRepository: ClienteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginClientUiState())
    val uiState: StateFlow<LoginClientUiState> = _uiState.asStateFlow()

    fun onEmailChange(text: String) {
        _uiState.value = _uiState.value.copy(email = text, errorMessage = null)
    }

    fun onPasswordChange(text: String) {
        _uiState.value = _uiState.value.copy(password = text, errorMessage = null)
    }

    fun login() {
        val state = _uiState.value

        // Validación simple de campos vacíos
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Ingresa tu correo y contraseña")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)

            // Llamamos al repositorio
            val result = clienteRepository.loginCliente(state.email, state.password)

            result.onSuccess { cliente ->
                // --- IMPORTANTE: GUARDAR SESIÓN GLOBAL ---
                SessionManager.login(cliente)
                // -----------------------------------------

                _uiState.value = state.copy(
                    isLoading = false,
                    loginSuccess = true,
                    clienteLogueado = cliente
                )
            }.onFailure { error ->
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Error al iniciar sesión"
                )
            }
        }
    }
}

