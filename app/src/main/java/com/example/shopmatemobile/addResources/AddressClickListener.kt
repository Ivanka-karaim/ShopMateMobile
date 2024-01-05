package com.example.shopmatemobile.addResources

interface AddressClickListener {
    fun deleteAddress(id: Int)

    fun editAddress(id: Int, city: String, street: String, house: String, flat: String?)
}