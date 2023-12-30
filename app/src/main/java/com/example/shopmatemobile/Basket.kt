package com.example.shopmatemobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.CheckboxAdapter
import com.example.shopmatemobile.addResources.BasketProductCountChangedListener
import com.example.shopmatemobile.addResources.CheckboxChangedListener
import com.example.shopmatemobile.databinding.FragmentBasketBinding
import com.example.shopmatemobile.model.OrderProduct
import com.example.shopmatemobile.service.BasketService
import com.example.shopmatemobile.service.PriceService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Basket.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basket : Fragment(), CheckboxChangedListener, BasketProductCountChangedListener  {
    lateinit var binding : FragmentBasketBinding
    private lateinit var adapter: CheckboxAdapter
    private lateinit var basket: List<OrderProduct>

    private var param1: String? = null
    private var param2: String? = null

    override fun onCheckboxChanged() {
        setPrice(adapter.getSelectedItems())
    }

    override fun onCountChanged() {
        setPrice(adapter.getSelectedItems())
    }

    private fun setPrice(checkboxes: List<OrderProduct>){
        binding.costBasket.text = PriceService.calcCost(checkboxes).toString()
        var discount: String = binding.spinnerCouponsBasket.selectedItem.toString()
        discount = discount.drop(1).dropLast(1)
        val price = binding.costBasket.text.toString().toDouble()
        val discountValue = discount.toDouble()
        val costDiscount = PriceService.calcCostDiscount(price, discountValue)
        binding.costDiscountBasket.text = costDiscount.toString()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = CheckboxAdapter(this, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding = FragmentBasketBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val spinnerArray = listOf(
            "-5%", "-1%"
        )
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray)
        binding.spinnerCouponsBasket.adapter = spinnerAdapter
        binding.spinnerCouponsBasket.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                var discount: String = binding.spinnerCouponsBasket.selectedItem.toString()
                discount = discount.drop(1).dropLast(1)
                val price = binding.costBasket.text.toString().toDouble()
                val discountValue = discount.toDouble()
                val costDiscount = PriceService.calcCostDiscount(price, discountValue)
                binding.costDiscountBasket.text = costDiscount.toString()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            basket = BasketService.getBasket(requireContext())
            if (isAdded) {
                requireActivity().runOnUiThread {
                    binding.apply {
                        adapter.submitList(basket)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Basket().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}