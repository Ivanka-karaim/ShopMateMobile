package com.example.shopmatemobile.model

class BasketCheckbox(val isChecked: Boolean,
                     val productId: String,
                     val productName: String,
                     val productImage: String,
                     val productRate: Double,
                     val productPrice: Double,
                     val count: Int,
    ){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = isChecked.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + productImage.hashCode()
        result = 31 * result + productRate.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + count
        return result
    }

}
