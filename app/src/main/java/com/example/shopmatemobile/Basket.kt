package com.example.shopmatemobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.CheckboxAdapter
import com.example.shopmatemobile.addResources.CheckboxChangedListener
import com.example.shopmatemobile.databinding.FragmentBasketBinding
import com.example.shopmatemobile.model.CouponStatus
import com.example.shopmatemobile.model.OrderProduct
import com.example.shopmatemobile.service.BasketService
import com.example.shopmatemobile.service.CouponsService
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
class Basket : Fragment(), CheckboxChangedListener {
    lateinit var binding : FragmentBasketBinding
    private lateinit var adapter: CheckboxAdapter
    private lateinit var basket: List<OrderProduct>

    private var param1: String? = null
    private var param2: String? = null

    override fun onCheckboxChanged() {
        setPrice(adapter.getSelectedItems())
    }
    private fun setPrice(checkboxes: List<OrderProduct>){
        binding.costBasket.text = PriceService.calcCost(checkboxes).toString()
        if (binding.spinnerCouponsBasket.selectedItemPosition!=0) {
            var discount: String = binding.spinnerCouponsBasket.selectedItem.toString()
            discount = discount.drop(1).dropLast(1)
            val price = binding.costBasket.text.toString().toDouble()
            val discountValue = discount.toDouble()
            val costDiscount = PriceService.calcCostDiscount(price, discountValue)
            binding.costDiscountBasket.text = costDiscount.toString()
        }else{
            binding.costDiscountBasket.text = binding.costBasket.text.toString()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = CheckboxAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding = FragmentBasketBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        val spinnerIds = mutableListOf<Int>()
        val spinnerArray = mutableListOf<String>()
        CoroutineScope(Dispatchers.IO).launch {
            val couponsStatus = CouponsService.getCouponsByStatus(requireContext(), "Активні")

            spinnerIds.add(0)
            if (couponsStatus == null) {
                spinnerArray.add("Купони відсутні")
            }
            else{
                spinnerArray.add("Купон не обрано")
                for (coupon in couponsStatus){
                    spinnerIds.add(coupon.coupon.id)
                    spinnerArray.add("-${coupon.coupon.discount.toInt()}%")
                }
            }

            val spinnerAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    spinnerArray
                )
            requireActivity().runOnUiThread {
                binding.apply {
                    binding.spinnerCouponsBasket.adapter = spinnerAdapter
                    binding.spinnerCouponsBasket.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                println(position)
                                if (position!=0) {

                                    var discount: String =
                                        binding.spinnerCouponsBasket.selectedItem.toString()
                                    discount = discount.drop(1).dropLast(1)
                                    val price = binding.costBasket.text.toString().toDouble()
                                    val discountValue = discount.toDouble()
                                    val costDiscount =
                                        PriceService.calcCostDiscount(price, discountValue)
                                    binding.costDiscountBasket.text = costDiscount.toString()
                                }else{
                                    binding.costDiscountBasket.text = binding.costBasket.text.toString()
                                }

                            }
                        }
                }
            }

            basket = BasketService.getBasket(requireContext())
            if (basket.isNotEmpty()) {
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        binding.apply {
                            adapter.submitList(basket)
                        }
                    }
                }
            }else{
                requireActivity().runOnUiThread {
                    binding.apply {
                        //
                    }
                }
            }
        }
        binding.buttonOrder.setOnClickListener{
            if (binding.costBasket.text.toString().toDouble()>0) {
                val intent = Intent(requireContext(), OrderCreation::class.java)
                val products = adapter.getSelectedItems()
                val productIds = ArrayList<String>()
                for (product in products){
                    productIds.add(product.id)
                }

                val couponId = spinnerIds[binding.spinnerCouponsBasket.selectedItemPosition]
                intent.putStringArrayListExtra("products", productIds)
                intent.putExtra("coupon", couponId.toString())
                startActivity(intent)
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