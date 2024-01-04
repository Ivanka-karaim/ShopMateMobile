package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Favourite
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FavouriteApi {

    @GET("favourites")
    suspend fun getFavourites(@Header("Authorization") token: String): Response<List<Favourite>>

    @GET("favourite")
    suspend fun checkFavourite(@Header("Authorization") token: String,@Query("productId") productId: String): Response<Boolean>

    @POST("favourite/change")
    suspend fun changeFavourite(@Body favourite: Favourite, @Header("Authorization") token:String): Response<ResponseBody>


}