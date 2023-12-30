package com.example.shopmatemobile.service

import com.example.shopmatemobile.model.OrderProduct
import kotlin.math.roundToInt

class PriceService {
    companion object {
        fun calcCostDiscount(price: Double, discount: Double): Double {
            val result = price * (1 - discount * 0.01)
            return (result * 100.0).roundToInt() / 100.0
        }

        fun calcCost(products: List<OrderProduct>): Double {
            var cost = 0.0
            for (product in products) {
                cost += product.price * product.count
            }
            return cost
        }
    }
}