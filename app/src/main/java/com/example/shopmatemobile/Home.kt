package com.example.shopmatemobile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.CategoryAdapter
import com.example.shopmatemobile.adapter.CheckboxAdapter
import com.example.shopmatemobile.adapter.FavouriteAdapter
import com.example.shopmatemobile.adapter.RadioAdapter
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.addResources.ErrorHandler
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.databinding.FragmentFavouriteBinding
import com.example.shopmatemobile.databinding.FragmentHomeBinding
import com.example.shopmatemobile.model.Favourite
import com.example.shopmatemobile.model.ProductShopMate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment(), ButtonClickListener{
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentHomeBinding
    private lateinit var adapterFavourite: FavouriteAdapter
    private lateinit var favourites: List<Favourite>
    private lateinit var productsModel: ArrayList<ProductShopMate>
    private lateinit var categories: List<String>
    private var minPrice: Double = 0.0
    private var maxPrice: Double = Double.MAX_VALUE
    private var jsonSelectFilter = mutableMapOf<String, String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonSelectFilter = mutableMapOf<String, String>()
        val layoutManager = object : GridLayoutManager(requireContext(), 2) {        }
        adapterFavourite = FavouriteAdapter(requireContext(), requireActivity())
        binding.RecyclerViewProduct.layoutManager = layoutManager
        binding.RecyclerViewProduct.adapter = adapterFavourite
        val favouriteApi = RetrofitClient.getInstance().create(FavouriteApi::class.java)
        val productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        val token = SharedPreferencesFactory(requireContext()).getToken()!!
        CoroutineScope(Dispatchers.IO).launch {
            val response = favouriteApi.getFavourites("Bearer " + token)
            if (response.isSuccessful) {
                favourites = response.body()!!
            }else{
                withContext(Dispatchers.Main){
                    if(response.code()==401){
                        ErrorHandler.unauthorizedUser(requireContext(), MainActivity())
                    }else{
                        ErrorHandler.generalError(requireContext())

                    }
                }

            }
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
                CoroutineScope(Dispatchers.IO).launch {
                    val products = productApi.searchProducts(enteredText).products
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
                    requireActivity().runOnUiThread { adapterFavourite.submitList(filterProducts(null)) }

                }

                true
            } else {
                false
            }
        }

        val dialog = Dialog(requireContext())
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params as WindowManager.LayoutParams
        dialog.setContentView(R.layout.custom_pop_up_filter)
        dialog.setCancelable(true)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recyclerViewSort = dialog.findViewById<RecyclerView>(R.id.recyclerViewSort)
        recyclerViewSort.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSort.adapter = RadioAdapter(listOf("Рекомендовані","Спочатку найдешевші","Спочатку найдорожчі" ),this, "sort")
        CoroutineScope(Dispatchers.IO).launch {
            val categoriesApi = productApi.getCategories()
            categories = listOf("All") + categoriesApi
            if (isAdded) {
                requireActivity().runOnUiThread {
                    binding.apply {
                        val adapter = RadioAdapter(categories, this@Home, "category")

                        recyclerView.adapter = adapter
                    }
                }
            }
        }

        val submitButton = dialog.findViewById<Button>(R.id.buttonSubmit)
        val cancelButton = dialog.findViewById<Button>(R.id.buttonCancel)
        submitButton.setOnClickListener {
            val products = filterProducts(dialog)
            adapterFavourite.submitList(products)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            adapterFavourite.submitList(productsModel)
            jsonSelectFilter = mutableMapOf<String, String>()
            dialog.dismiss()
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
            val products = productApi.searchProducts(searchText.toString()).products
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
            requireActivity().runOnUiThread {
                binding.apply {
                    adapterFavourite.submitList(productsModel)

                }
            }
        }
    }

    fun filterProducts(dialog: Dialog?): ArrayList<ProductShopMate>{
        var products = filtered()
        println(products)
        println(products.size)
        if(dialog!=null) {
            val minPrice = dialog.findViewById<EditText>(R.id.minPrice)
            val maxPrice = dialog.findViewById<EditText>(R.id.maxPrice)
            if (minPrice.text.isNotEmpty()) {
                this.minPrice = minPrice.text.toString().toDouble()
            }else{
                this.minPrice = 0.0
            }
            if (maxPrice.text.isNotEmpty()) {
                this.maxPrice = maxPrice.text.toString().toDouble()
            }else{
                this.maxPrice = Double.MAX_VALUE
            }
            println(products.size)
        }
        products = products.filter {
            it.price >= minPrice
        } as ArrayList<ProductShopMate>
        products = products.filter {
            it.price <= maxPrice
        } as ArrayList<ProductShopMate>

        if (jsonSelectFilter.get("sort")!=null){
            val sort = jsonSelectFilter.get("sort")
            if (sort == "Спочатку найдешевші") {
                products = products.sortedBy { it.price }.toCollection(ArrayList())
                println(products.size)
            }
            else if(sort == "Спочатку найдорожчі") {
                products = products.sortedByDescending { it.price }.toCollection(ArrayList())
                println(products.size)
            }
        }
        println(products.size)
        println(products)

        return products
    }

    override fun onButtonClick(category: String, filter: String?) {
        jsonSelectFilter.put(filter!!, category)
    }

    fun filtered(): ArrayList<ProductShopMate>{
        println(jsonSelectFilter)
        var filteredProducts = productsModel
        if (jsonSelectFilter.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                jsonSelectFilter.any { json ->
                    when (json.key) {
                        "category" -> {if(json.value!="All") product.category == json.value else true }
                        "brand" -> product.brand == json.value
                        "sort" -> false
                        else -> true
                    }
                }
            } as ArrayList<ProductShopMate>
        }

        return filteredProducts

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}