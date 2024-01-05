package com.example.shopmatemobile.addResources

import android.content.Context
class SharedPreferencesFactory(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPreferences.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun clearToken(){
        sharedPreferences.edit().remove("TOKEN").apply()
    }
}