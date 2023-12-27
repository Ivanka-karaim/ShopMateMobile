package com.example.shopmatemobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.CategoryAdapter
import com.example.shopmatemobile.adapter.FavouriteAdapter
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.RetrofitClient2
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.databinding.FragmentFavouriteBinding
import com.example.shopmatemobile.model.ProductShopMate
import com.example.shopmatemobile.service.FavouriteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    // TODO: Rename and change types of parameters
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
        adapterFavourite = FavouriteAdapter(requireContext())
        binding.RecyclerViewProduct.layoutManager = layoutManager
        binding.RecyclerViewProduct.adapter = adapterFavourite

        CoroutineScope(Dispatchers.IO).launch {
            favourites = FavouriteService.getFavourites(requireContext())
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

    override fun onButtonClick(category: String){
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