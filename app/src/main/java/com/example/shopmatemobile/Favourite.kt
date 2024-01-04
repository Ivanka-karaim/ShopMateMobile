package com.example.shopmatemobile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.CategoryAdapter
import com.example.shopmatemobile.adapter.FavouriteAdapter
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.addResources.ErrorHandler
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.FavouriteApi
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.ReviewApi
import com.example.shopmatemobile.databinding.FragmentFavouriteBinding
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
 * Use the [Favourite.newInstance] factory method to
 * create an instance of this fragment.
 */
class Favourite : Fragment(), ButtonClickListener {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentFavouriteBinding
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterFavourite: FavouriteAdapter

    private lateinit var favourites: List<ProductShopMate>

    private lateinit var categories: List<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCategory = CategoryAdapter(requireContext(), "All", this)
        binding.RecyclerViewCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerViewCategory.adapter = adapterCategory
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val desiredItemWidth = screenWidth / 2

        val layoutManager = object : GridLayoutManager(requireContext(), 2) {
            fun getMeasuredWidth(): Int {
                return desiredItemWidth
            }
        }
        adapterFavourite = FavouriteAdapter(requireContext(), requireActivity())
        binding.RecyclerViewProduct.layoutManager = layoutManager
        binding.RecyclerViewProduct.adapter = adapterFavourite

        CoroutineScope(Dispatchers.IO).launch {
            favourites = getFavourites()
            if (favourites.isNotEmpty()) {
                var catg = mutableSetOf<String>()
                catg.add("All")
                favourites.forEach { product ->
                    catg.add(product.category)
                }
                categories = catg.toList()


                if (isAdded) {
                    requireActivity().runOnUiThread {
                        binding.apply {
                            emptyFavourite.visibility = View.INVISIBLE
                            adapterFavourite.submitList(favourites)
                            adapterCategory.submitList(categories)
                        }
                    }
                }
            } else {
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        binding.apply {
                            emptyFavourite.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onButtonClick(category: String, filter: String?) {
        println(56789)
        println(category)
        if(category=="All") {
            adapterFavourite.submitList(favourites)

        }else{
            val filteredList = favourites.filter { it.category == category }
            adapterFavourite.submitList(filteredList)
        }
        adapterCategory = CategoryAdapter(requireContext(),  category, this)
        binding.RecyclerViewCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerViewCategory.adapter = adapterCategory
        adapterCategory.submitList(categories)
    }

    suspend fun getFavourites(): List<ProductShopMate> {
        var productsShopMate = ArrayList<ProductShopMate>()
        var favouriteApi = RetrofitClient.getInstance().create(FavouriteApi::class.java)
        var productApi = RetrofitClient2.getInstance().create(ProductApi::class.java)
        var reviewApi = RetrofitClient.getInstance().create(ReviewApi::class.java)
        return withContext(Dispatchers.IO) {
            println(SharedPreferencesFactory(requireContext()).getToken())
            var response = favouriteApi.getFavourites("Bearer "+ SharedPreferencesFactory(requireContext()).getToken()!!)
            if (response.isSuccessful) {
                var favourites = response.body()

                if (favourites != null) {
                    if (favourites.isNotEmpty()) {
                        for (favourite in favourites) {
                            val product = productApi.getProductById(favourite.productId)
                            val response = reviewApi.getGradeForProduct(
                                favourite.productId,
                                "Bearer " + SharedPreferencesFactory(requireContext()).getToken()!!
                            )
                            if(response.isSuccessful) {
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
                                        grade = response.body()!!,
                                        isFavourite = true
                                    )
                                )
                            }else{
                                if(response.code()==401){
                                    ErrorHandler.unauthorizedUser(requireContext(), requireActivity())
                                }else{
                                    ErrorHandler.generalError(requireContext())
                                }
                            }
                        }
                    }
                }
                return@withContext productsShopMate;
            }else{
                if(response.code()==401){
                    ErrorHandler.unauthorizedUser(requireContext(), requireActivity())
                }else{
                    ErrorHandler.generalError(requireContext())
                }
                return@withContext emptyList()
            }
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Favourite.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Favourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}