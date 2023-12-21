package com.example.shopmatemobile.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi{
    @GET("getRating")
    suspend fun getGradeForProduct(@Query("idProduct") idProduct: String, @Header("Authorization") token: String): Double
}