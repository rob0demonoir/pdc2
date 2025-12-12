package com.duoc.principedecolores

import com.duoc.principedecolores.data.model.Product
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductTest {

    @Test
    fun `isOutOfStock devuelve TRUE cuando el stock es 0`() {
        val productoAgotado = Product(
            id = 1, name = "Jabón", description = "", price = 100,
            stock = 0, // <--- Stock Cero
            imageUri = null, createdAt = 0
        )

        assertTrue("El producto debería estar agotado", productoAgotado.isOutOfStock)
    }

    @Test
    fun `isOutOfStock devuelve FALSE cuando hay stock positivo`() {
        val productoDisponible = Product(
            id = 1, name = "Jabón", description = "", price = 100,
            stock = 5, // <--- Hay Stock
            imageUri = null, createdAt = 0
        )

        assertFalse("El producto NO debería estar agotado", productoDisponible.isOutOfStock)
    }
}