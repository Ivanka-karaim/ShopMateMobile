package com.example.shopmatemobile.api

import com.example.shopmatemobile.model.Address
import com.example.shopmatemobile.model.Basket
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AddressApi {
    @GET("profile/addresses")
    suspend fun getAddresses(@Header("Authorization") token: String): Response<List<Address>>

    @DELETE("profile/address/delete")
    suspend fun deleteAddresses(@Header("Authorization") token: String, @Query("addressId") addressId: Int): Response<ResponseBody>

    @PATCH("profile/address/edit")
    suspend fun editAddresses(@Header("Authorization") token: String, @Body userAddressModel: Address): Response<ResponseBody>

    @POST("profile/address/add")
    suspend fun addAddress(@Header("Authorization") token: String, @Body userAddressModel: Address): Response<ResponseBody>


}