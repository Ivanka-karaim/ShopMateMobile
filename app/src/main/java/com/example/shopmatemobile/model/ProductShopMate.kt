package com.example.shopmatemobile.model

data class ProductShopMate(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
    val grade: Double,
    var isFavourite: Boolean

)
