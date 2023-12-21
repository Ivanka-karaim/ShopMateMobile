package com.example.shopmatemobile.model

class Coupon (
    val couponId:Int,
    val discount:Double,
    val status:String,
    val dateExpiration:String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}