package com.example.shopmatemobile.model

class CouponStatus(val coupon: Coupon, val status:String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CouponStatus

        if (coupon != other.coupon) return false
        return status == other.status
    }

    override fun hashCode(): Int {
        var result = coupon.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}