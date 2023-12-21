package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Favourite
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface FavouriteApi {

    @GET("favourites")
    suspend fun getFavourites(@Header("Authorization") token: String): List<Favourite>

    @POST("favourite/change")
    suspend fun changeFavourite(@Body favourite: Favourite, @Header("Authorization") token:String)


}