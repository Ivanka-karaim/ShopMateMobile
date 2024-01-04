package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.OrderAdapter
import com.example.shopmatemobile.adapter.OrderProductAdapter
import com.example.shopmatemobile.databinding.ActivityOrderBinding
import com.example.shopmatemobile.databinding.ActivityOrdersBinding
import com.example.shopmatemobile.model.OrderInfo
import com.example.shopmatemobile.service.CouponsService
import com.example.shopmatemobile.service.OrderService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrdersBinding
    private var adapter: OrderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupExpandableListView()

        binding.ordersRadiogroup.setOnCheckedChangeListener{_, checkedId ->
            val radio = findViewById<RadioButton>(checkedId)
            CoroutineScope(Dispatchers.IO).launch {
                getOrders(radio.text.toString())
            }
        }
    }
    private fun getOrders(status:String?){
        CoroutineScope(Dispatchers.IO).launch {
            val orders = OrderService.getOrders(applicationContext, status)
            if (orders.isNotEmpty()) {

                val titleList = mutableListOf<String>()
                for (order in orders) {
                    titleList.add(order.date)

                }
                runOnUiThread {
                    binding.apply {
                        binding.ordersError.text = ""
                        adapter = OrderAdapter(applicationContext, titleList, orders)
                        binding.expandableListView.visibility = View.VISIBLE
                        binding.expandableListView.setAdapter(adapter)
                    }
                }
            }else{
                runOnUiThread {
                    binding.ordersError.text = "У вас поки немає таких замовлень"
                    binding.expandableListView.visibility = View.INVISIBLE
                }
            }
        }
    }


    private fun setupExpandableListView() {

        getOrders("Активні")

        binding.expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                "List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.expandableListView.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                "List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        /*binding.expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
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