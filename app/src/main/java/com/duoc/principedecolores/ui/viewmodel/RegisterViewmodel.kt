package com.duoc.principedecolores.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null // Para errores de API (ej: "Servidor caído")
)

class RegisterViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).{8,}$"
    )

    fun onNombreChange(text: String) {
        _uiState.value = _uiState.value.copy(nombre = text, nameError = null, generalError = null)
    }

    fun onEmailChange(text: String) {
        _uiState.value = _uiState.value.copy(email = text, emailError = null, generalError = null)
    }

    fun onPasswordChange(text: String) {
        _uiState.value = _uiState.value.copy(password = text, passwordError = null, generalError = null)
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return PASSWORD_PATTERN.matcher(password).matches()
    }

    fun registrar() {
        val state = _uiState.value
        var isValid = true

        var currentState = state.copy(
            nameError = null, emailError = null, passwordError = null, generalError = null
        )


        if (state.nombre.isBlank()) {
            currentState = currentState.copy(nameError = "El nombre es obligatorio")
            isValid = false
        }

        if (state.email.isBlank()) {
            currentState = currentState.copy(emailError = "El correo es obligatorio")
            isValid = false
        } else if (!isEmailValid(state.email)) {
            currentState = currentState.copy(emailError = "Formato de correo inválido")
            isValid = false
        }

        if (state.password.isBlank()) {
            currentState = currentState.copy(passwordError = "La contraseña es obligatoria")
            isValid = false
        } else if (!isPasswordValid(state.password)) {
            currentState = currentState.copy(
                passwordError = "Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo"
            )
            isValid = false
        }

        _uiState.value = currentState

        if (!isValid) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val resultado = repository.registrarCliente(state.nombre, state.email, state.password)

            resultado.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generalError = error.message // Error global (ej. "Email ya existe")
                )
            }
        }
    }
}