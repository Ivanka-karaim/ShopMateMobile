package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Review
import com.example.shopmatemobile.model.ReviewForAdd
import com.example.shopmatemobile.model.ReviewProduct
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi{
    @GET("getRating")
    suspend fun getGradeForProduct(@Query("idProduct") idProduct: String, @Header("Authorization") token: String): Double

    @GET("getListRating")
    suspend fun getListGradeForProduct(@Query("idProducts") idProducts: List<String>, @Header("Authorization") token: String): List<ReviewProduct>

    @GET("reviews")
    suspend fun getReviews(@Header("Authorization") token: String, @Query("idProduct") idProduct: String ): List<Review>

    @DELETE("review/delete")
    suspend fun deleteReview(@Header("Authorization") token: String,@Query("reviewId") idReview: Int)

    @POST("review/add")
    suspend fun addReview(@Header("Authorization") token: String,@Body reviewForAdd: ReviewForAdd)
}