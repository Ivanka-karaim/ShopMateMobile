package com.example.shopmatemobile.service

import android.content.Context
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.ReviewApi
import com.example.shopmatemobile.model.ProductShopMate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create

object FavouriteService {

    suspend fun getFavourites(context: Context): List<ProductShopMate> {
        var productsShopMate = ArrayList<ProductShopMate>()
        var favouriteApi = RetrofitClient.getInstance().create(FavouriteApi::class.java)
        var productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        var reviewApi = RetrofitClient.getInstance().create(ReviewApi::class.java)
        return withContext(Dispatchers.IO) {
            println(SharedPreferencesFactory(context).getToken())
            var favourites = favouriteApi.getFavourites("Bearer "+SharedPreferencesFactory(context).getToken()!!)

            if (favourites != null) {
                for (favourite in favourites) {
                    val product = productApi.getProductById(favourite.productId)
                    val grade = reviewApi.getGradeForProduct(favourite.productId, "Bearer "+SharedPreferencesFactory(context).getToken()!!)
                    productsShopMate.add(
                        ProductShopMate(
                            id = product.id,
                            title = product.title,
                            description = product.description,
                            price = product.price,
                            brand = product.brand,
                            category = product.category,
                            thumbnail = product.thumbnail,
                            images = product.images,
                            grade = grade,
                            isFavourite = true
                        )
                    )
                }
            }
            return@withContext productsShopMate;


        }
    }
}