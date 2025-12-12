package com.duoc.principedecolores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.principedecolores.data.api.ExternalRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class HoroscopeUiState(
    val selectedSign: String = "",
    val prediction: String = "",
    val date: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HoroscopoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HoroscopeUiState())
    val uiState: StateFlow<HoroscopeUiState> = _uiState.asStateFlow()

    // Lista de signos para el Dropdown/Grid
    val signs = listOf(
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    )

    // Traducción simple para mostrar en español
    fun translateSign(sign: String): String {
        return when(sign) {
            "Aries" -> "Aries ♈"
            "Taurus" -> "Tauro ♉"
            "Gemini" -> "Géminis ♊"
            "Cancer" -> "Cáncer ♋"
            "Leo" -> "Leo ♌"
            "Virgo" -> "Virgo ♍"
            "Libra" -> "Libra ♎"
            "Scorpio" -> "Escorpio ♏"
            "Sagittarius" -> "Sagitario ♐"
            "Capricorn" -> "Capricornio ♑"
            "Aquarius" -> "Acuario ♒"
            "Pisces" -> "Piscis ♓"
            else -> sign
        }
    }

    fun getHoroscopo(sign: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedSign = sign,
                error = null,
                prediction = ""
            )

            try {

                val response = ExternalRetrofitClient.apiService.getHoroscopo(sign.lowercase())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    prediction = response.data.prediction,
                    date = response.data.date
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "El universo no responde... revisa tu internet."
                )
            }
        }
    }
}