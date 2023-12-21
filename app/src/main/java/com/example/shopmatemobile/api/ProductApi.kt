package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Category
import com.example.shopmatemobile.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String) : Product

    @GET("products/categories")
    suspend fun getCategories(): List<String>

}