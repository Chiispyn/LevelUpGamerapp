package com.levelupgamer.app.model

data class Product(
    val code: String,
    val name: String,
    val category: String,
    val price: Int,
    val description: String = "",
    val quantity: Int = 1,
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val ratingCount: Int = 0
)