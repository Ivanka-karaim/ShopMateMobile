package com.example.shopmatemobile.service

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.shopmatemobile.MainActivity
import com.example.shopmatemobile.R
import com.example.shopmatemobile.SignIn
import com.example.shopmatemobile.addResources.ErrorHandler
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.AddressApi
import com.example.shopmatemobile.model.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressService(var context: Context, var activity: Activity) {
    private var addressApi = RetrofitClient.getInstance().create(AddressApi::class.java)


    fun editAddress(
        id: Int,
        city: String,
        street: String,
        house: String,
        flat: String?

    ) {
        var token = SharedPreferencesFactory(context).getToken()!!

        CoroutineScope(Dispatchers.IO).launch {
            val response = addressApi.editAddresses(
                "Bearer $token",
                Address(
                    id,
                    city, street, house, flat

                )
            )
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    if (response.code() == 401) {
                        ErrorHandler.unauthorizedUser(context, activity)
                    } else {
                        ErrorHandler.generalError(context)
                    }
                }
            }

        }


    }


    fun addAddress(
        cityNew: String,
        streetNew: String,
        houseNew: String,
        flatNew: String
    ) {
        var token = SharedPreferencesFactory(context).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            val response = addressApi.addAddress(
                "Bearer " + token,
                Address(
                    0,
                    cityNew,
                    streetNew,
                    houseNew,
                    flatNew
                )
            )
            if(!response.isSuccessful){
                if(response.code()==401){
                    ErrorHandler.unauthorizedUser(context, activity)
                }else{
                    ErrorHandler.generalError(context)
                }
            }
        }
    }

    fun deleteAddress(id: Int) {
        var token = SharedPreferencesFactory(context).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            var response = addressApi.deleteAddresses("Bearer " + token, id)
            if(!response.isSuccessful){
                if(response.code()==401){
                    ErrorHandler.unauthorizedUser(context, activity)
                }else{
                    ErrorHandler.generalError(context)
                }
            }
        }
    }

    suspend fun getAddress(): List<Address> {
        var token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {

            var response = addressApi.getAddresses("Bearer $token")
            if(response.isSuccessful){
                return@withContext response.body()!!
            }else{
                if(response.code()==401){
                    ErrorHandler.unauthorizedUser(context, activity)
                }else{
                    ErrorHandler.generalError(context)
                }
            }
            return@withContext emptyList()

        }
    }


    fun checkCityAndStreet(editText: EditText, error: TextView): Boolean {
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(editText.text.toString())) {
            editText.text.clear()
            error.text = "Може складатись лише з літер"
            return false
        }
        return true
    }

    fun checkHouseAndFlat(editText: EditText, error: TextView): Boolean {
        val regex = Regex("[0-9]+")
        if (!regex.matches(editText.text.toString())) {
            editText.text.clear()
            error.text = "Може складатись лише з цифр"
            return false
        }
        return true
    }
}

