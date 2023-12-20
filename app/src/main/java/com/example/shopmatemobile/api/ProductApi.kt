package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Category
import com.example.shopmatemobile.model.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products/1")
    suspend fun getProductById() : Product

    @GET("products/categories")
    suspend fun getCategories(): List<String>

}