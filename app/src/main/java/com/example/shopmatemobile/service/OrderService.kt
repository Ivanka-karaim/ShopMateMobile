package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.OrderApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.CreateOrder
import com.example.shopmatemobile.model.OrderCreation
import com.example.shopmatemobile.model.OrderInfo
import com.example.shopmatemobile.model.OrderInfoProducts
import com.example.shopmatemobile.model.OrderProduct
import com.example.shopmatemobile.model.Status
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

    suspend fun createOrder(context: Context, orderInfo:CreateOrder):Int{
        val orderApi = RetrofitClient.getInstance().create(OrderApi::class.java)
        val token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {
            val createOrder = orderApi.createOrder("Bearer $token", orderInfo)
            return@withContext createOrder

        }

    }

    suspend fun getOrderById(context: Context, id:Int):OrderInfo{
        val orderApi = RetrofitClient.getInstance().create(OrderApi::class.java)
        val token = SharedPreferencesFactory(context).getToken()!!
        return withContext(Dispatchers.IO) {
            println("in service")
            println(id)
            val orderById = orderApi.getOrderById("Bearer $token", id)
            return@withContext orderById;
        }

    }

    suspend fun getOrders(context: Context, status:String?):List<OrderInfoProducts>{
        val orderApi = RetrofitClient.getInstance().create(OrderApi::class.java)
        val token = SharedPreferencesFactory(context).getToken()!!
        val orderInfoProducts = mutableListOf<OrderInfoProducts>()
        return withContext(Dispatchers.IO) {
                val orders = orderApi.getOrders("Bearer $token", status)
                for (order in orders) {
                    orderInfoProducts.add(
                        OrderInfoProducts(
                            order.orderId,
                            order.date,
                            order.totalPrice,
                            getOrderProducts(order.productBaskets)
                        )
                    )
                }
                return@withContext orderInfoProducts;
        }

    }
    suspend fun getOrderStatus(context: Context, statusId:Int):Status{
        val allStatuses = listOf(Status(0, "Замовлення оформлене", R.drawable.created), Status(1, "В обробці", R.drawable.container), Status(2, "Відправлено", R.drawable.truck), Status(3, "Доставлено", R.drawable.box), Status(4, "Скасовано", R.drawable.baseline_close_24))
        for (status in allStatuses){
            if (status.id == statusId){
                return status
            }
        }
        return allStatuses.last()

    }
}