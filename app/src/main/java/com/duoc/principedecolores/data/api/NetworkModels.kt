package com.duoc.principedecolores.data.api

import com.duoc.principedecolores.data.model.Cliente
import com.google.gson.annotations.SerializedName

// Para el Login (Body del POST)
data class LoginRequest(
    val username: String,
    val password: String
)

// Para agregar al carrito (Body del POST)
data class AnadirAlCarritoRequest(
    val idProducto: Int,
    val cantidad: Int,
    val idCliente: Int // <--- ¡Nuevo campo obligatorio!
)

// IMPORTANTE: Para recibir el carrito anidado desde Spring Boot
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

// ... tus otras clases ...

// 1. Lo que enviamos para iniciar sesión
data class LoginClienteRequest(
    val email: String,
    val password: String
)

// 2. Lo que el servidor nos responde si el login es correcto
data class LoginClienteResponse(
    val mensaje: String, // Ej: "Login exitoso"
    val token: String?,   // Opcional: Si usaras JWT en el futuro
    val cliente: Cliente  // <--- ¡Aquí usamos tu nuevo modelo!
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