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

// 1. Estado con errores SEPARADOS
data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,

    // Variables de error específicas
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null // Para errores de API (ej: "Servidor caído")
)

class RegisterViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // 2. REGEX MEJORADO (Agregado (?=.*[0-9]) para números)
    private val PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).{8,}$"
    )

    // Cuando el usuario escribe, limpiamos el error DE ESE CAMPO solamente
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

        // Reseteamos errores previos
        var currentState = state.copy(
            nameError = null, emailError = null, passwordError = null, generalError = null
        )

        // --- VALIDACIONES INDIVIDUALES ---

        // 1. Nombre
        if (state.nombre.isBlank()) {
            currentState = currentState.copy(nameError = "El nombre es obligatorio")
            isValid = false
        }

        // 2. Email
        if (state.email.isBlank()) {
            currentState = currentState.copy(emailError = "El correo es obligatorio")
            isValid = false
        } else if (!isEmailValid(state.email)) {
            currentState = currentState.copy(emailError = "Formato de correo inválido")
            isValid = false
        }

        // 3. Contraseña
        if (state.password.isBlank()) {
            currentState = currentState.copy(passwordError = "La contraseña es obligatoria")
            isValid = false
        } else if (!isPasswordValid(state.password)) {
            currentState = currentState.copy(
                passwordError = "Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo"
            )
            isValid = false
        }

        // Actualizamos el estado con los errores detectados
        _uiState.value = currentState

        // Si hay algún error, detenemos la función aquí
        if (!isValid) return

        // --- LLAMADA A LA API ---
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