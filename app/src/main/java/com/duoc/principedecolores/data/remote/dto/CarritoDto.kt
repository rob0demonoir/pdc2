package com.duoc.principedecolores.data.remote.dto

// Representa un ítem del carrito tal como viene de la API
data class CarritoDto(
    val id: Int,
    val producto: ProductDto, // El objeto producto anidado
    val cantidad: Int,
    val anadido: Long
)

// Representa el cuerpo de la petición para agregar un item al carrito
data class AddToCartRequest(
    val idProducto: Int,
    val cantidad: Int
)