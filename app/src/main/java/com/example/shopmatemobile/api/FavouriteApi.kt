package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Favourite
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface FavouriteApi {

    @Headers()
    @GET("favourites")
    suspend fun getFavourites(@Header("Authorization") token: String): List<Favourite>


}