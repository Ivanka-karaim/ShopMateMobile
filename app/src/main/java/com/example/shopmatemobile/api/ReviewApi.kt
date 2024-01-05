package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Review
import com.example.shopmatemobile.model.ReviewForAdd
import com.example.shopmatemobile.model.ReviewProduct
import okhttp3.ResponseBody
import retrofit2.Response
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
    suspend fun getGradeForProduct(@Query("idProduct") idProduct: String, @Header("Authorization") token: String): Response<Double>

    @GET("reviews")
    suspend fun getReviews(@Header("Authorization") token: String, @Query("idProduct") idProduct: String ): Response<List<Review>>

    @DELETE("review/delete")
    suspend fun deleteReview(@Header("Authorization") token: String,@Query("reviewId") idReview: Int): Response<ResponseBody>

    @POST("review/add")
    suspend fun addReview(@Header("Authorization") token: String,@Body reviewForAdd: ReviewForAdd): Response<ResponseBody>
}