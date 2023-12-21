package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.CouponsAdapter
import com.example.shopmatemobile.databinding.ActivityCouponsBinding
import com.example.shopmatemobile.model.Coupon

class Coupons : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupons)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val coupons = listOf(
            Coupon(1, 20.0, "активний", "20.04.2033" ),
            Coupon(2, 10.0, "активний", "20.04.2043" ),
        )

        val layoutManager = LinearLayoutManager(this)
        val adapter = CouponsAdapter()
        adapter.submitList(coupons)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}