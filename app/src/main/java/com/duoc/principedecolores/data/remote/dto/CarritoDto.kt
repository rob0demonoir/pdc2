package com.duoc.principedecolores.data.remote.dto

data class CarritoDto(
    val id: Int,
    val producto: ProductDto, // El objeto producto anidado
    val cantidad: Int,
    val anadido: Long
)

data class AddToCartRequest(
    val idProducto: Int,
    val cantidad: Int
)