package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.BasketApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.ReviewApi
import com.example.shopmatemobile.model.BasketCheckbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BasketService {
    suspend fun getBasket(context: Context): List<BasketCheckbox> {
        val productsBasket = ArrayList<BasketCheckbox>()
        val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
        val productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        val reviewApi = RetrofitClient.getInstance().create(ReviewApi::class.java)
        return withContext(Dispatchers.IO) {
            println(SharedPreferencesFactory(context).getToken())
            val basket =
                basketApi.getBasket("Bearer " + SharedPreferencesFactory(context).getToken()!!)

            if (basket.isNotEmpty()) {
                for (basketItem in basket) {
                    val product = productApi.getProductById(basketItem.productId)
                    /*val gradeProduct = reviewApi.getGradeForProduct(basketItem.productId, "Bearer "+SharedPreferencesFactory(context).getToken()!!)*/
                    productsBasket.add(
                        BasketCheckbox(
                            isChecked = false,
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
}