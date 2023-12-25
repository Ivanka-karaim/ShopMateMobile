package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Basket
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface BasketApi {
    @GET("basket")
    suspend fun getBasket(@Header("Authorization") token: String): List<Basket>
}