package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.EditProfile
import com.example.shopmatemobile.model.PasswordChange
import com.example.shopmatemobile.model.Profile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): Profile

    @PATCH("profile/edit")
    suspend fun editProfile(@Body editProfile: EditProfile, @Header("Authorization") token:String): Response<ResponseBody>

    @PATCH("profile/password/change")
    suspend fun changePassword(@Body passwordChange: PasswordChange, @Header("Authorization") token:String): Response<ResponseBody>
}