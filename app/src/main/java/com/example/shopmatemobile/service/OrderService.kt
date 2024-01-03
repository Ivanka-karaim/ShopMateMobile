package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.OrderApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.CreateOrder
import com.example.shopmatemobile.model.OrderCreation
import com.example.shopmatemobile.model.OrderProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OrderService {
    suspend fun getOrderInfo(context: Context, productsIds: ArrayList<String>): OrderCreation {

        val orderApi = RetrofitClient.getInstance().create(OrderApi::class.java)

        val token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {
            return@withContext orderApi.getOrderInfo("Bearer $token", productsIds);
        }

    }

    suspend fun getOrderProducts(products: List<Basket>): MutableList<OrderProduct> {
        val productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        val productsOrder = mutableListOf<OrderProduct>()
        for (product in products) {
            val productGet = productApi.getProductById(product.productId)
            val productOrder = OrderProduct(
                id = productGet.id.toString(),
                title = productGet.title,
                thumbnail = productGet.thumbnail,
                price = productGet.price.toDouble(),
                grade = productGet.rating.toDouble(),
                count = product.number
            )
            productsOrder.add(productOrder)
        }
        return productsOrder
    }

    suspend fun createOrder(context: Context, orderInfo:CreateOrder){
        val orderApi = RetrofitClient.getInstance().create(OrderApi::class.java)
        val token = SharedPreferencesFactory(context).getToken()!!
        val createOrder = orderApi.createOrder("Bearer $token", orderInfo)
        if (createOrder.isSuccessful){
            println("successful")
        }

    }
}