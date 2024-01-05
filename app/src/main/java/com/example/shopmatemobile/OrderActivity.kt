package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.OrderProductAdapter
import com.example.shopmatemobile.databinding.ActivityOrderBinding
import com.example.shopmatemobile.service.OrderService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Замовлення"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        val adapter = OrderProductAdapter(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val orderId = intent.getStringExtra("orderId")

        CoroutineScope(Dispatchers.IO).launch {
            val order = OrderService.getOrderById(applicationContext, orderId!!.toInt())
            val products = OrderService.getOrderProducts(order.productBaskets)
            val orderStatus = OrderService.getOrderStatus(applicationContext, order.status)
            runOnUiThread {
                binding.apply {
                    adapter.submitList(products)
                    binding.orderDate.text = order.date
                    binding.orderCost.text = order.totalPrice.toString()
                    binding.orderStatus.text = orderStatus.title
                    binding.imageStatusIcon.setImageResource(orderStatus.image)
                }
            }
        }




    }
}