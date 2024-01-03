package com.example.shopmatemobile.service

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.ServiceCallback
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.AddressApi
import com.example.shopmatemobile.model.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AddressService {
    private var addressApi = RetrofitClient.getInstance().create(AddressApi::class.java)


    fun editAddress(
        id: Int,
        city: String,
        street: String,
        house: String,
        flat: String?,
        context: Context
    ) {
        var token = SharedPreferencesFactory(context).getToken()!!

        CoroutineScope(Dispatchers.IO).launch {
            addressApi.editAddresses(
                "Bearer " + token,
                Address(
                    id,
                    city, street, house, flat

                )
            )
        }


    }


    fun addAddress(context: Context, cityNew: String,streetNew: String, houseNew: String, flatNew: String ) {
        var token = SharedPreferencesFactory(context).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            AddressService.addressApi.addAddress(
                "Bearer " + token,
                Address(
                    0,
                    cityNew,
                    streetNew,
                    houseNew,
                    flatNew
                )
            )
        }
    }

    fun deleteAddress(id: Int, context: Context) {
        var token = SharedPreferencesFactory(context).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            addressApi.deleteAddresses("Bearer " + token, id)
        }
    }

    suspend fun getAddress(context: Context): List<Address> {
        var token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {
            addressApi.getAddresses("Bearer $token")
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