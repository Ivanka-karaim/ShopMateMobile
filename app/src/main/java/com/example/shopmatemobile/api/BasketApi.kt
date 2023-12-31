package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.ProductId
import com.squareup.okhttp.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface BasketApi {
    @GET("basket")
    suspend fun getBasket(@Header("Authorization") token: String): List<Basket>

    @POST("basket/add")
    suspend fun addToBasket(@Header("Authorization") token: String, @Body basket: Basket)

    @HTTP(method = "DELETE", path = "basket/delete", hasBody = true)
    suspend fun deleteFromBasket(@Header("Authorization") token: String, @Body basket: Basket)

    @HTTP(method = "DELETE", path ="basket/remove")
    suspend fun removeFromBasket(@Header("Authorization") token: String, @Query("productId") productId:String)
}