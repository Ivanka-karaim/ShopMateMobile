package com.example.shopmatemobile.service

import android.app.Activity
import android.content.Context
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.BasketApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.OrderProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object BasketService {
    suspend fun getBasket(context: Context): List<OrderProduct> {
        val productsBasket = ArrayList<OrderProduct>()
        val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
        val productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        return withContext(Dispatchers.IO) {
            val basket =
                basketApi.getBasket("Bearer " + SharedPreferencesFactory(context).getToken()!!)

            if (basket.isNotEmpty()) {
                for (basketItem in basket) {
                    val product = productApi.getProductById(basketItem.productId)
                    productsBasket.add(
                        OrderProduct(
                            id = product.id.toString(),
                            title = product.title,
                            price = product.price.toDouble(),
                            thumbnail = product.thumbnail,
                            grade = product.rating.toDouble(),
                            count = basketItem.number
                        )
                    )
                }
            }
            return@withContext productsBasket
        }
    }

    suspend fun addToBasket(context: Context, id:String){
        val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
        val token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {
            basketApi.addToBasket("Bearer $token", Basket(id, 1))
        }
    }

}