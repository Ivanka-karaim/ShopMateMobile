package com.example.shopmatemobile

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.IntentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.OrderProductAdapter
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.AddressApi
import com.example.shopmatemobile.databinding.ActivityOrderCreationBinding
import com.example.shopmatemobile.model.CreateOrder
import com.example.shopmatemobile.model.UserOrder
import com.example.shopmatemobile.service.AddressService
import com.example.shopmatemobile.service.CouponsService
import com.example.shopmatemobile.service.OrderService
import com.example.shopmatemobile.service.PriceService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OrderCreation : AppCompatActivity() {
    lateinit var binding: ActivityOrderCreationBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Оформлення замовлення"
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

        val coupon = intent.getStringExtra("coupon")
        val productsBasket = intent.getStringArrayListExtra("products")


        val currentContext = this

        val spinnerCoupon: Spinner = findViewById(R.id.spinnerCoupons)
        val spinnerIds = mutableListOf<Int>()
        val spinnerArray = mutableListOf<String>()
        CoroutineScope(Dispatchers.IO).launch {
            val couponsStatus = CouponsService.getCouponsByStatus(currentContext, "Активні")

            spinnerIds.add(0)
            if (couponsStatus == null) {
                spinnerArray.add("Купони відсутні")
            } else {
                spinnerArray.add("Купон не обрано")
                for (couponStatus in couponsStatus) {
                    spinnerIds.add(couponStatus.coupon.id)
                    spinnerArray.add("-${couponStatus.coupon.discount.toInt()}%")
                }
            }
        }
        val spinnerAdapter =
            ArrayAdapter(
                currentContext,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
            )
        val token = SharedPreferencesFactory(this).getToken()!!
        val spinnerAddress: Spinner = findViewById(R.id.spinnerAddress)
        val spinnerArrayAddress = mutableListOf<String>()
        val spinnerArrayAddressIds = mutableListOf<Int>()
        val addressApi = RetrofitClient.getInstance().create(AddressApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = addressApi.getAddresses("Bearer $token")
            if(response.isSuccessful) {
                var errorText = ""
                val addresses = response.body()!!
                if (addresses.isNotEmpty()) {
                    for (address in addresses) {
                        spinnerArrayAddressIds.add(address.id)
                        spinnerArrayAddress.add("м.${address.city} вул.${address.house} буд.${address.house} кв.${address.flat}")
                    }
                }
                else{
                    errorText = "У вас немає доданих адрес."

                }
                runOnUiThread {
                    binding.apply {
                        binding.errorAddress.text = errorText
                    }
                }
            }
        }

        val spinnerAdapterAddress =
            ArrayAdapter(
                currentContext,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArrayAddress
            )

        binding.buttonAddAddress.setOnClickListener{
            this.addAddress()
        }

        binding.buttonPay.setOnClickListener {
            clean()
            val checkName = checkName()
            val checkSurname = checkSurname()
            val checkPhoneNumber = checkPhoneNumber()
            val checkAddress = checkAddress()
            if (checkName && checkSurname && checkPhoneNumber && checkAddress) {
                CoroutineScope(Dispatchers.IO).launch {
                    val userOrder = UserOrder(binding.editName.text.toString(),
                        binding.editSurname.text.toString(), binding.editPhoneNumber.text.toString())
                    var couponId:Int? = spinnerIds[spinnerCoupon.selectedItemPosition]
                    if (couponId == 0){
                        couponId = null
                    }
                    val selectedAddress = spinnerArrayAddressIds[spinnerAddress.selectedItemPosition]
                    val orderInfo = CreateOrder(productsBasket as List<String>, userOrder, couponId, selectedAddress)
                    val order = OrderService.createOrder(currentContext, orderInfo)
                    val intent = Intent(currentContext, OrderActivity::class.java)
                    intent.putExtra("orderId", order.toString())
                    finish()
                    startActivity(intent)
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (productsBasket != null) {
                val orderInfo = OrderService.getOrderInfo(currentContext, productsBasket)
                val user = UserOrder(
                    orderInfo.user.firstName,
                    orderInfo.user.lastName, orderInfo.user.phoneNumber
                )
                val products = OrderService.getOrderProducts(orderInfo.products)
                runOnUiThread {
                    binding.apply {
                        adapter.submitList(products)
                        spinnerCoupon.adapter = spinnerAdapter
                        spinnerAddress.adapter = spinnerAdapterAddress
                        binding.editName.setText(user.firstName)
                        binding.editSurname.setText(user.lastName)
                        binding.editPhoneNumber.setText(user.phoneNumber)
                        binding.costOrder.text = PriceService.calcCost(products.toList()).toString()

                        if (coupon != null) {
                            spinnerCoupon.setSelection(spinnerIds.indexOf(coupon.toInt()))
                        }

                    }
                }
            }


            spinnerCoupon.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val price = binding.costOrder.text.toString().toDouble()
                    var costDiscount = price
                    if (position != 0) {
                        var discount: String = spinnerCoupon.selectedItem.toString()
                        discount = discount.drop(1).dropLast(1)
                        val discountValue = discount.toDouble()
                        costDiscount = PriceService.calcCostDiscount(price, discountValue)
                    }

                    runOnUiThread {
                        binding.apply {
                            binding.costDiscountOrder.text = costDiscount.toString()
                        }
                    }
                }
            }
        }
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

    fun checkAddress(): Boolean {
        if(binding.spinnerAddress.selectedItem==null){
            binding.errorAddress.text="Щоб зробити замовлення, додайте адресу"
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

    fun addAddress() {
        val dialog = Dialog(this)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        dialog.setTitle(null)
        dialog.setContentView(R.layout.custom_pop_up_add_address)
        dialog.setCancelable(true)
        dialog.findViewById<TextView>(R.id.changeAddress).text = "Додавання адреси"
        val closeButton = dialog.findViewById<ImageButton>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        if(!isFinishing) {
            dialog.show()
        }
        val buttonChangeAddressSubmit =
            dialog.findViewById<Button>(R.id.button_change_address_submit)
        buttonChangeAddressSubmit.setOnClickListener {
            val cityNew = dialog.findViewById<EditText>(R.id.old_city)
            val errorCity = dialog.findViewById<TextView>(R.id.error_city)
            errorCity.text = ""
            val streetNew = dialog.findViewById<EditText>(R.id.old_street)
            val errorStreet = dialog.findViewById<TextView>(R.id.error_street)
            errorCity.text = ""
            val houseNew = dialog.findViewById<EditText>(R.id.old_house)
            val errorHouse = dialog.findViewById<TextView>(R.id.error_house)
            errorHouse.text = ""
            val flatNew = dialog.findViewById<EditText>(R.id.old_flat)
            val errorFlat = dialog.findViewById<TextView>(R.id.error_flat)
            errorFlat.text = ""

            if (AddressService(this, this).checkCityAndStreet(
                    cityNew,
                    errorCity
                ) && AddressService(this, this).checkCityAndStreet(
                    streetNew,
                    errorStreet
                ) && AddressService(this, this).checkHouseAndFlat(
                    houseNew,
                    errorHouse
                ) && (flatNew.text.toString().isEmpty() || AddressService(this, this).checkHouseAndFlat(
                    flatNew,
                    errorFlat
                ))
            ) {
                AddressService(this, this).addAddress(

                    cityNew.text.toString(),
                    streetNew.text.toString(),
                    houseNew.text.toString(),
                    flatNew.text.toString()
                )
                recreate()
            }
        }
    }

}