package com.example.shopmatemobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.OrderProductAdapter
import com.example.shopmatemobile.databinding.ActivityOrderCreationBinding
import com.example.shopmatemobile.model.OrderProduct


class OrderCreation : AppCompatActivity() {
    lateinit var binding: ActivityOrderCreationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val products = listOf(
            OrderProduct("2", "Text", "", 5.0, 200.0, 1),
            OrderProduct("3", "Text1", "", 5.0, 2100.0, 1),
        )
        binding.costOrder.text = calcCost(products).toString()
        println(binding.costOrder.text)
        val spinnerCoupon: Spinner = findViewById(R.id.spinnerCoupons)
        val spinnerArray = listOf(
            "-5%", "-1%"
        )
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray)
        spinnerCoupon.adapter = spinnerAdapter
        spinnerCoupon.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                var discount: String = spinnerCoupon.selectedItem.toString()
                discount = discount.drop(1).dropLast(1)
                val price = binding.costOrder.text.toString().toDouble()
                val discountValue = discount.toDouble()
                val costDiscount = calcCostDiscount(price, discountValue)
                binding.costDiscountOrder.text = costDiscount.toString()
            }
        }

        val spinnerAddress: Spinner = findViewById(R.id.spinnerAddress)
        val spinnerArrayAddress = listOf(
            "вул. Янгеля буд 8", "вул Шевченка бкл 7"
        )
        val spinnerAdapterAddress =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayAddress)
        spinnerAddress.adapter = spinnerAdapterAddress

        binding.buttonPay.setOnClickListener {
            clean()
            val checkName = checkName()
            val checkSurname = checkSurname()
            val checkPhoneNumber = checkPhoneNumber()
            if (checkName && checkSurname && checkPhoneNumber){
                println("ok")
            }
        }

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

            val layoutManager = LinearLayoutManager(this)
            val adapter = OrderProductAdapter()
            adapter.submitList(products)

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        fun calcCostDiscount(price: Double, discount: Double): Double {
            return price * (1 - discount * 0.01)
        }

        fun calcCost(products: List<OrderProduct>): Double {
            var cost = 0.0
            for (product in products) {
                cost += product.productPrice * product.count
            }
            return cost

    }

    fun clean() {
        binding.errorName.text = ""
        binding.errorSurname.text = ""
        binding.errorPhoneNumber.text = ""
    }

    fun checkName(): Boolean {
        val nameText = binding.editName.text.toString().trim()
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(nameText)) {
            binding.editName.text.clear()
            binding.errorName.text = "Ім'я може складатись лише з літер"
            return false
        }
        return true

    }

    fun checkSurname(): Boolean {
        val surnameText = binding.editSurname.text.toString().trim()
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(surnameText)) {
            binding.editSurname.text.clear()
            binding.errorSurname.text = "Прізвище може складатись лише з літер"
            return false
        }
        return true
    }

    fun checkPhoneNumber(): Boolean {
        val phoneNumberText = binding.editPhoneNumber.text.toString().trim()
        val regex =
            Regex("^\\+?\\d{1,3}[\\s-]?(\\(\\d{3}\\)|\\d{3})[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}\$")
        if (!regex.matches(phoneNumberText)) {
            binding.editPhoneNumber.text.clear()
            binding.errorPhoneNumber.text = "Такого номера телефону не існує"
            return false
        }
        return true
    }

}