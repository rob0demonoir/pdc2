package com.duoc.principedecolores.data.model

data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val imageUri: String? = null, // URI de la imagen guardada
    val createdAt: Long = System.currentTimeMillis()
) {
    val isOutOfStock: Boolean
        get() = stock <= 0
}