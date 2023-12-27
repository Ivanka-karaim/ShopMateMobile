package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Category
import com.example.shopmatemobile.model.Product
import com.example.shopmatemobile.model.ProductList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String) : Product

    @GET("products?limit=0")
    suspend fun getProducts() : ProductList
    @GET("products/categories")
    suspend fun getCategories(): List<String>

}