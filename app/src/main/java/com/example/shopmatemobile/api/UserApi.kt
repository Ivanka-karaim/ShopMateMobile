package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.SignInModel
import com.example.shopmatemobile.model.UserToken
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("signin")
    suspend fun signIn(@Body signInModel: SignInModel): UserToken
}