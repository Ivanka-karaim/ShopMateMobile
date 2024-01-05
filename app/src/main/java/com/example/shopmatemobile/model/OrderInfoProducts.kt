package com.example.shopmatemobile.model

class OrderInfoProducts(val orderId:Int, val date:String, val totalPrice:Double,
val productsOrder:List<OrderProduct>) {
}