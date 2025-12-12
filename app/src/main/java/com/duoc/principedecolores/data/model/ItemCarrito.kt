package com.duoc.principedecolores.data.model

data class ItemCarrito(
    val id: Int = 0,
    val idProducto: Int,
    val nombreProducto: String,
    val precioProducto: Int,
    val uriImagenProducto: String?,
    val cantidad: Int,
    val anadido: Long = System.currentTimeMillis()
){
    val subtotal: Int
        get() = precioProducto * cantidad
}