package com.example.shopmatemobile.model

class OrderProduct(val productId: String,
                   val productName: String,
                   val productImage: String,
                   val productRate: Double,
                   val productPrice: Double,
                   val count: Int){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderProduct

        if (productId != other.productId) return false
        if (productName != other.productName) return false
        if (productImage != other.productImage) return false
        if (productRate != other.productRate) return false
        if (productPrice != other.productPrice) return false
        return count == other.count
    }

    override fun hashCode(): Int {
        var result = productId.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + productImage.hashCode()
        result = 31 * result + productRate.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + count
        return result
    }
}