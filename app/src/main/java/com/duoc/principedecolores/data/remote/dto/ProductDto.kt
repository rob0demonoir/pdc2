package com.duoc.principedecolores.data.remote.dto

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val imageUri: String?,
    val createdAt: Long
)