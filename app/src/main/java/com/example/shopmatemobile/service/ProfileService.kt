package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.ProfileApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProfileService {
    suspend fun getProfile(context: Context):com.example.shopmatemobile.model.Profile {
        val profileApi = RetrofitClient.getInstance().create(ProfileApi::class.java)
        return withContext(Dispatchers.IO) {
            return@withContext profileApi.getProfile("Bearer " + SharedPreferencesFactory(context).getToken()!!);
        }
    }
}