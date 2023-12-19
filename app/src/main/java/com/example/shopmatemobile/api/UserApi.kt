package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.SignInModel
import com.example.shopmatemobile.model.User
import com.example.shopmatemobile.model.UserToken
import com.google.android.gms.common.api.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("signin")
    suspend fun signIn(@Body signInModel: SignInModel): retrofit2.Response<UserToken>

    @POST("signup")
    suspend fun signUp(@Body user: User): UserToken

}