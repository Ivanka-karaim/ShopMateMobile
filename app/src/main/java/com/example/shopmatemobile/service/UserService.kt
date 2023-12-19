package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.model.SignInModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserService {

//    fun signIn(userApi: UserApi, email: String, password: String, context: Context): String {
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = userApi.signIn(
//                SignInModel(
//                    email, password
//                )
//            )
//            if (response.isSuccessful) {
//                val token = response.body()
//                SharedPreferencesFactory(context).saveToken(token!!.token)
//                return@launch "good"
//
//            } else {
//                return@launch response.errorBody().toString()
//            }
//        }
//        userApi.enqueue(object : Callback<UserData> {
//            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//                if (response.isSuccessful) {
//                    // Обробка успішного відгуку
//                } else {
//                    val error = response.errorBody()?.string()
//                    // Обробка помилки
//                    // Можна парсити `error` як JSON у вашому ErrorResponse класі
//                }
//            }
//
//            override fun onFailure(call: Call<UserData>, t: Throwable) {
//                // Обробка невдалого запиту
//            }
//        })
//    }

    fun signUp() {

    }

    fun logout() {

    }


}