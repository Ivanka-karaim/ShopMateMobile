package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopmatemobile.adapter.FavouriteAdapter
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.ReviewApi
import com.example.shopmatemobile.databinding.ActivityProductsBinding
import com.example.shopmatemobile.model.ProductShopMate
import com.example.shopmatemobile.service.FavouriteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.create

class ProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductsBinding
    private lateinit var adapterFavourite: FavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Товари"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val desiredItemWidth = screenWidth / 2

        val layoutManager = object : GridLayoutManager(this, 2) {
            fun getMeasuredWidth(): Int {
                return desiredItemWidth
            }
        }
        adapterFavourite = FavouriteAdapter(this)
        binding.RecyclerViewProduct.layoutManager = layoutManager
        binding.RecyclerViewProduct.adapter = adapterFavourite

        var favouriteApi = RetrofitClient.getInstance().create(FavouriteApi::class.java)
        var productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        var reviewApi = RetrofitClient.getInstance().create(ReviewApi::class.java)
        var token = SharedPreferencesFactory(this).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            var favourites = favouriteApi.getFavourites("Bearer "+token)



            var products = productApi.getProducts().products
//            var grades = reviewApi.getListGradeForProduct(products.map { it.id.toString() },  "Bearer "+token)

            var listProducts = ArrayList<ProductShopMate>()

            for (product in products) {
                listProducts.add(
                    ProductShopMate(
                        id = product.id,
                        title = product.title,
                        description = product.description,
                        price = product.price,
                        brand = product.brand,
                        category = product.category,
                        thumbnail = product.thumbnail,
                        images = product.images,
                        isFavourite = favourites.any { it.productId == product.id.toString() },
                        grade = 0.0
                    )
                )
            }


            runOnUiThread {
                binding.apply {
                    adapterFavourite.submitList(listProducts)

                }

            }
        }
    }
}