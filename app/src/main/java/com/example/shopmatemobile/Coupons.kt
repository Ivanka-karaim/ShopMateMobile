package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.CouponsAdapter
import com.example.shopmatemobile.databinding.ActivityCouponsBinding
import com.example.shopmatemobile.model.Coupon
import com.example.shopmatemobile.service.CouponsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Coupons : AppCompatActivity() {
    lateinit var binding: ActivityCouponsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        val adapter = CouponsAdapter()
        val currentContext = this
        CoroutineScope(Dispatchers.IO).launch {
            val coupons = CouponsService.getCouponsByStatus(currentContext, "Активні")
            runOnUiThread {
                binding.apply {
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    adapter.submitList(coupons)
                }
            }
        }
        binding.couponsRadiogroup.setOnCheckedChangeListener{_, checkedId ->
        val radio = findViewById<RadioButton>(checkedId)
            CoroutineScope(Dispatchers.IO).launch {
                println(radio.text.toString())
                val coupons = CouponsService.getCouponsByStatus(currentContext, radio.text.toString())
                runOnUiThread {
                    binding.apply {

                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = adapter
                        adapter.submitList(coupons)
                    }
                }
            }
        }

    }
}