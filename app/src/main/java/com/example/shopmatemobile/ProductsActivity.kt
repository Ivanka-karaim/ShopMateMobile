package com.example.shopmatemobile

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.CategoryAdapter
import com.example.shopmatemobile.adapter.FavouriteAdapter
import com.example.shopmatemobile.adapter.RadioAdapter
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.databinding.ActivityProductsBinding

import com.example.shopmatemobile.model.Favourite
import com.example.shopmatemobile.model.ProductShopMate
import com.example.shopmatemobile.service.FavouriteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.create

class ProductsActivity : AppCompatActivity(), ButtonClickListener {
    lateinit var binding: ActivityProductsBinding
    private lateinit var adapterFavourite: FavouriteAdapter
    private lateinit var favourites: List<Favourite>
    private lateinit var productsModel: ArrayList<ProductShopMate>
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var categories: List<String>
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var nowProducts: ArrayList<ProductShopMate>

    private var jsonSelectFilter= mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jsonSelectFilter = mutableMapOf<String, String>()
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
        var token = SharedPreferencesFactory(this).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            favourites = favouriteApi.getFavourites("Bearer " + token)
        }
        getProducts(productApi)

        binding.searchChange.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED
            ) {
                val enteredText = binding.searchChange.text.toString()
                println(enteredText)
                getProducts(productApi, enteredText)

                true
            } else {
                false
            }
        }
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_pop_up_filter)
        dialog.setCancelable(true)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        CoroutineScope(Dispatchers.IO).launch {
            var categoriesApi = productApi.getCategories()
            categories = listOf("All") + categoriesApi
            runOnUiThread {
                binding.apply {
                    val adapter = RadioAdapter(categories, this@ProductsActivity, "category")
                    recyclerView.adapter = adapter
                }
            }
        }



        binding.filterButton.setOnClickListener {
            val closeButton = dialog.findViewById<ImageButton>(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()


        }
    }


    fun getProducts(productApi: ProductApi, searchText: String? = "") {
        CoroutineScope(Dispatchers.IO).launch {
            var products = productApi.searchProducts(searchText.toString()).products
            productsModel = ArrayList<ProductShopMate>()
            for (product in products) {
                productsModel.add(
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
                    adapterFavourite.submitList(productsModel)

                }
            }
        }
    }

    override fun onButtonClick(category: String, filter: String?) {
        println(jsonSelectFilter)
        if(category=="All"){
            jsonSelectFilter.remove(filter!!)
        }else {
            jsonSelectFilter.put(filter!!, category)
        }
        var products = filtered()
        adapterFavourite.submitList(products)

    }

    fun filtered(): ArrayList<ProductShopMate>{
        println(jsonSelectFilter)
        var filteredProducts = productsModel
        if (jsonSelectFilter.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                jsonSelectFilter.any { json ->
                    when (json.key) {
                        "category" -> product.category == json.value
                        "brand" -> product.brand == json.value
                        else -> true
                    }
                }
            } as ArrayList<ProductShopMate>
        }

        return filteredProducts

    }



}