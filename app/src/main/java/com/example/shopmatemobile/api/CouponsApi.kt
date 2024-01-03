package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Coupon
import com.example.shopmatemobile.model.OrderCreation
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CouponsApi {
    @GET("/profile/coupons")
    suspend fun getCouponsByStatus(@Header("Authorization") token: String, @Query("statusCoupon") statusCoupon: Int): List<Coupon>

}