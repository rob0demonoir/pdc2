package com.duoc.principedecolores.data.api

import com.duoc.principedecolores.data.model.Cliente
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class AnadirAlCarritoRequest(
    val idProducto: Int,
    val cantidad: Int,
    val idCliente: Int // <--- ¡Nuevo campo obligatorio!
)

data class CarritoResponse(
    val id: Int,
    val cantidad: Int,
    val anadido: Long,
    val producto: ProductResponse // Objeto anidado
)

data class ProductResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val imageUri: String?,
    val createdAt: Long
)

data class RegistroClienteRequest(
    val nombre: String,
    val email: String,
    val password: String
)


data class LoginClienteRequest(
    val email: String,
    val password: String
)

data class LoginClienteResponse(
    val mensaje: String,
    val token: String?,
    val cliente: Cliente
)

data class ItemCarritoResponse(
    val id: Int,
    val cantidad: Int,
    val anadido: Long,
    val producto: ProductResponse
)

data class CarritoCompletoResponse(
    val items: List<ItemCarritoResponse>,
    val totalGeneral: Int,
    val cantidadProductos: Int
)