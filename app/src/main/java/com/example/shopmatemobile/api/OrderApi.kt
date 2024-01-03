package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.CreateOrder
import com.example.shopmatemobile.model.Favourite
import com.example.shopmatemobile.model.OrderCreation
import com.squareup.okhttp.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi {
    @GET("/orders/info")
    suspend fun getOrderInfo(@Header("Authorization") token: String, @Query("ProductsId") productsId: ArrayList<String>): OrderCreation

    @POST("/order/create")
    suspend fun createOrder(@Header("Authorization") token: String, @Body createOrder: CreateOrder): Response<ResponseBody>
}