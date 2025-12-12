/**
REPOSITORY PARA ALMACENAMIENTO DE DATOS EN LOCAL


package com.duoc.principedecolores.data.repository

import com.duoc.principedecolores.data.dao.CarritoDao
import com.duoc.principedecolores.data.model.Carrito
import kotlinx.coroutines.flow.Flow

class CarritoRepository (private val carritoDao: CarritoDao){

val obtenerCarrito: Flow<List<Carrito>> = carritoDao.obtenerCarrito()

val contarJabonesCarrito: Flow<Int> = carritoDao.contarJabonesCarrito()

suspend fun obtenerJabonesPorId(productId: Int): Carrito?{
return carritoDao.obtenerJabonesPorId(productId)
}

suspend fun anadirAlCarrito(carrito: Carrito) {
val jabonExistente = carritoDao.obtenerJabonesPorId(carrito.idProducto)
if (jabonExistente != null) {

carritoDao.actualizarJabonCarrito(
jabonExistente.copy(cantidad = jabonExistente.cantidad + carrito.cantidad)
)
} else {

carritoDao.insertJabon(carrito)
}
}

suspend fun actualizarJabonCarrito(carrito: Carrito) {
carritoDao.actualizarJabonCarrito(carrito)
}

suspend fun borrarJabonCarrito(carrito: Carrito) {
carritoDao.borrarJabonCarrito(carrito)
}

suspend fun eliminarCarrito() {
carritoDao.eliminarCarrito()
}
}

 **/

package com.duoc.principedecolores.data.repository

import android.util.Log
import com.duoc.principedecolores.data.api.AnadirAlCarritoRequest
import com.duoc.principedecolores.data.api.RetrofitClient
import com.duoc.principedecolores.data.model.ItemCarrito
import com.duoc.principedecolores.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CarritoRepository {

    val obtenerCarrito: Flow<List<ItemCarrito>> = flow {
        try {

            if (!SessionManager.isLoggedIn()) {
                emit(emptyList()) // Si no está logueado, carrito vacío
                return@flow
            }
            val userId = SessionManager.getClientId()

            val response = RetrofitClient.apiService.getCarrito(userId)

            if (response.isSuccessful && response.body() != null) {
                val carritoBackend = response.body()!!
                val itemsBackend = carritoBackend.items

                val listaMapeada = itemsBackend.map { item ->
                    ItemCarrito(
                        id = item.id,
                        idProducto = item.producto.id,
                        nombreProducto = item.producto.name,
                        precioProducto = item.producto.price,
                        uriImagenProducto = item.producto.imageUri,
                        cantidad = item.cantidad,
                        stock = item.producto.stock,
                        anadido = item.anadido
                    )
                }
                emit(listaMapeada)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    val contarJabonesCarrito: Flow<Int> = flow {
        try {
            if (!SessionManager.isLoggedIn()) {
                emit(0)
                return@flow
            }
            val userId = SessionManager.getClientId()
            val response = RetrofitClient.apiService.getCarrito(userId)
            if (response.isSuccessful) {
                emit(response.body()?.cantidadProductos ?: 0)
            } else {
                emit(0)
            }
        } catch (e: Exception) { emit(0) }
    }

    suspend fun anadirAlCarrito(itemCarrito: ItemCarrito) {
        try {
            if (!SessionManager.isLoggedIn()) return // Seguridad

            val request = AnadirAlCarritoRequest(
                idProducto = itemCarrito.idProducto,
                cantidad = itemCarrito.cantidad,
                idCliente = SessionManager.getClientId() // <--- ¡AQUÍ ENVIAMOS EL ID!
            )
            RetrofitClient.apiService.addToCart(request)
        } catch (e: Exception) { Log.e("Carrito", "Error añadir", e) }
    }

    suspend fun procesarCompra(): Boolean {
        return try {
            if (!SessionManager.isLoggedIn()) return false
            val response = RetrofitClient.apiService.procesarPago(SessionManager.getClientId())
            response.isSuccessful
        } catch (e: Exception) { false }
    }

    suspend fun borrarJabonCarrito(carrito: ItemCarrito) {
        try {
            RetrofitClient.apiService.deleteCartItem(carrito.id)
        } catch (e: Exception) { Log.e("Carrito", "Error borrar", e) }
    }
}