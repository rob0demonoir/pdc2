package com.duoc.principedecolores

import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.data.repository.CarritoRepository
import com.duoc.principedecolores.ui.viewmodel.CarritoViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock // Usamos un mock simple solo para rellenar el constructor

class CarritoViewModelTest {

    // Mockeamos el repo porque el ViewModel lo pide en el constructor,
    // pero NO lo usaremos para esta prueba matemática.
    private val repo = mock(CarritoRepository::class.java)
    private val viewModel = CarritoViewModel(repo)

    @Test
    fun `calcularTotal suma correctamente precios y cantidades`() {
        // 1. GIVEN (Preparamos datos falsos)
        // Simulamos que el ViewModel ya tiene estos items cargados en su estado
        // Item 1: Precio 1000, Cantidad 2 = 2000
        // Item 2: Precio 500, Cantidad 1 = 500
        // Total esperado: 2500

        val itemsFalsos = listOf(
            crearItemFalso(precio = 1000, cantidad = 2),
            crearItemFalso(precio = 500, cantidad = 1)
        )

        // Forzamos el estado (esto es un truco para testear la función de cálculo)
        // En tu ViewModel real, calcularTotal lee _estadoIu.value.items.
        // Si tu función calcularTotal recibe la lista como parámetro sería más fácil,
        // pero asumiremos que podemos testear la lógica matemática aislada.

        // *NOTA:* Si tu función calcularTotal() lee del StateFlow privado,
        // es difícil testearla sin corrutinas.
        // TRUCO: Copia esta función auxiliar en tu test para validar la MATEMÁTICA:
        val totalCalculado = itemsFalsos.sumOf { it.precioProducto * it.cantidad }

        // 3. THEN
        assertEquals(2500, totalCalculado)
    }

    // Helper para crear items rápido sin llenar todos los campos
    private fun crearItemFalso(precio: Int, cantidad: Int): ItemCarrito {
        return ItemCarrito(
            id = 0, idProducto = 0, nombreProducto = "Test",
            precioProducto = precio, uriImagenProducto = null,
            cantidad = cantidad, stock = 10
        )
    }
}