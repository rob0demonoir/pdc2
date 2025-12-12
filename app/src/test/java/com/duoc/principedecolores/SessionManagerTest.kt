package com.duoc.principedecolores

import com.duoc.principedecolores.data.model.Cliente
import com.duoc.principedecolores.utils.SessionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionManagerTest {

    @Test
    fun `Flujo completo de Sesion Login y Logout`() {
        // 1. Al principio no debería haber nadie
        SessionManager.logout() // Nos aseguramos de limpiar
        assertFalse(SessionManager.isLoggedIn())

        // 2. Hacemos Login
        val clientePrueba = Cliente(id = 1, nombre = "Juan", email = "juan@test.com")
        SessionManager.login(clientePrueba)

        // Verificamos que esté logueado
        assertTrue(SessionManager.isLoggedIn())
        assertEquals(1, SessionManager.getClientId())

        // 3. Hacemos Logout
        SessionManager.logout()

        // Verificamos que se haya borrado
        assertFalse(SessionManager.isLoggedIn())
    }
}