package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.OrderAdapter
import com.example.shopmatemobile.adapter.OrderProductAdapter
import com.example.shopmatemobile.databinding.ActivityOrderBinding
import com.example.shopmatemobile.databinding.ActivityOrdersBinding
import com.example.shopmatemobile.model.OrderInfo
import com.example.shopmatemobile.service.OrderService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrdersBinding
    private var adapter: OrderAdapter? = null
    private var titleList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupExpandableListView()
    }


    private fun setupExpandableListView() {
        val expandableListView = binding.expandableListView

        CoroutineScope(Dispatchers.IO).launch {
            val orders = OrderService.getOrders(applicationContext)
            for (order in orders) {
                titleList.add(order.date)

            }
            runOnUiThread {
                binding.apply {
                    adapter = OrderAdapter(applicationContext, titleList, orders)
                    expandableListView.setAdapter(adapter)
                }
            }
        }

        expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        /*expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Toast.makeText(
                applicationContext,
                "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
                    childPosition
                ),
                Toast.LENGTH_SHORT
            ).show()
            false
        }*/
    }

}