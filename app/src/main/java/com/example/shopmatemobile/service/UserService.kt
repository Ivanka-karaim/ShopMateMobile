package com.example.shopmatemobile.service

import android.content.Context
import android.content.Intent
import com.example.shopmatemobile.MainActivity2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.model.SignInModel
import com.example.shopmatemobile.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

object UserService {

    suspend fun signIn(
        userApi: UserApi,
        email: String,
        password: String,
        context: Context
    ): String {

        return withContext(Dispatchers.IO) {
            val response = userApi.signIn(
                SignInModel(
                    email, password
                )
            )
            if (response.isSuccessful) {
                val token = response.body()
                SharedPreferencesFactory(context).saveToken(token!!.token)

            } else {
                val errorMessage = response.errorBody()?.string()
                println(errorMessage)
                if (errorMessage.toString().contains("UserNoFound")) {
                    return@withContext "emailError"


                } else if (errorMessage.toString().contains("WrongPassword")) {
                    return@withContext "passwordError"

                }
            }
            return@withContext ""
        }
    }


    fun logout() {

    }
}


