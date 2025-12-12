package com.duoc.principedecolores

import com.duoc.principedecolores.ui.viewmodel.HoroscopoViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class HoroscopoViewModelTest {

    // Instanciamos el ViewModel (como no tiene parámetros en el constructor, es fácil)
    private val viewModel = HoroscopoViewModel()

    @Test
    fun `translateSign devuelve el signo con emoji correcto para Aries`() {
        // 1. GIVEN (Dado un valor de entrada)
        val input = "Aries"

        // 2. WHEN (Cuando ejecutamos la función)
        val result = viewModel.translateSign(input)

        // 3. THEN (Entonces esperamos este resultado exacto)
        assertEquals("Aries ♈", result)
    }

    @Test
    fun `translateSign devuelve el signo con emoji correcto para Piscis`() {
        val input = "Pisces"
        val result = viewModel.translateSign(input)
        assertEquals("Piscis ♓", result)
    }

    @Test
    fun `translateSign devuelve el mismo texto si el signo no existe`() {
        val input = "Ofiuco" // Un signo que no está en la lista
        val result = viewModel.translateSign(input)

        // Debería devolver lo mismo porque entra en el 'else'
        assertEquals("Ofiuco", result)
    }
}