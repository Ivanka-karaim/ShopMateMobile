package com.example.shopmatemobile.model

class OrderInfo(val orderId:Int, val date:String, val couponDiscount:Double, val totalPrice:Double,
                val productBaskets:List<Basket>, val status:Int) {
}