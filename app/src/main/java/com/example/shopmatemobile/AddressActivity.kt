package com.example.shopmatemobile

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.AddressAdapter
import com.example.shopmatemobile.addResources.AddressClickListener
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.AddressApi
import com.example.shopmatemobile.databinding.ActivityAddressBinding
import com.example.shopmatemobile.model.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddressActivity : AppCompatActivity(), AddressClickListener {
    lateinit var binding: ActivityAddressBinding
    lateinit var token: String
    lateinit var addressApi: AddressApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Адреси"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        token = SharedPreferencesFactory(this).getToken()!!

        addressApi = RetrofitClient.getInstance().create(AddressApi::class.java)
        val recyclerView = binding.RecyclerViewAddresses
        recyclerView.layoutManager =
            LinearLayoutManager(this@AddressActivity, LinearLayoutManager.VERTICAL, false)
        val adapter = AddressAdapter(this@AddressActivity, this@AddressActivity)
        recyclerView.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val addresses = addressApi.getAddresses("Bearer " + token)

            runOnUiThread {
                binding.apply {
                    println(addresses)
                    adapter.submitList(addresses)
                }
            }
        }
        binding.addAddress.setOnClickListener {
            addAddress()
        }
    }


    override fun deleteAddress(id: Int) {
        println(id)
        CoroutineScope(Dispatchers.IO).launch {
            addressApi.deleteAddresses("Bearer " + token, id)
        }
        finish()
        startActivity(intent)
    }

    override fun editAddress(id: Int, city: String, street: String, house: String, flat: String?) {
        val dialog = Dialog(this)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        dialog.setTitle(null)
        dialog.setContentView(R.layout.custom_pop_up_add_address)
        dialog.setCancelable(true)
        dialog.findViewById<EditText>(R.id.old_city).setText(city)
        dialog.findViewById<EditText>(R.id.old_street).setText(street)
        dialog.findViewById<EditText>(R.id.old_house).setText(house)
        dialog.findViewById<EditText>(R.id.old_flat).setText(flat)
        val closeButton = dialog.findViewById<ImageButton>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        val buttonChangeAddressSubmit =
            dialog.findViewById<Button>(R.id.button_change_address_submit)
        buttonChangeAddressSubmit.setOnClickListener {
            var cityNew = dialog.findViewById<EditText>(R.id.old_city)
            val errorCity = dialog.findViewById<TextView>(R.id.error_city)
            errorCity.text = ""
            var streetNew = dialog.findViewById<EditText>(R.id.old_street)
            val errorStreet = dialog.findViewById<TextView>(R.id.error_street)
            errorCity.text = ""
            var houseNew = dialog.findViewById<EditText>(R.id.old_house)
            val errorHouse = dialog.findViewById<TextView>(R.id.error_house)
            errorHouse.text = ""
            var flatNew = dialog.findViewById<EditText>(R.id.old_flat)
            val errorFlat = dialog.findViewById<TextView>(R.id.error_flat)
            errorFlat.text = ""
            if (checkCityAndStreet(cityNew, errorCity) && checkCityAndStreet(
                    streetNew,
                    errorStreet
                ) && checkHouseAndFlat(houseNew, errorHouse) && checkHouseAndFlat(
                    flatNew,
                    errorFlat
                )
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    addressApi.editAddresses(
                        "Bearer " + token,
                        Address(
                            id,
                            cityNew.text.toString(),
                            streetNew.text.toString(),
                            houseNew.text.toString(),
                            flatNew.text.toString()
                        )
                    )

                }
                finish()
                startActivity(intent)
            }
        }
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
        dialog.show()
        val buttonChangeAddressSubmit =
            dialog.findViewById<Button>(R.id.button_change_address_submit)
        buttonChangeAddressSubmit.setOnClickListener {
            var cityNew = dialog.findViewById<EditText>(R.id.old_city)
            val errorCity = dialog.findViewById<TextView>(R.id.error_city)
            errorCity.text=""
            var streetNew = dialog.findViewById<EditText>(R.id.old_street)
            val errorStreet = dialog.findViewById<TextView>(R.id.error_street)
            errorCity.text=""
            var houseNew = dialog.findViewById<EditText>(R.id.old_house)
            val errorHouse = dialog.findViewById<TextView>(R.id.error_house)
            errorHouse.text=""
            var flatNew = dialog.findViewById<EditText>(R.id.old_flat)
            val errorFlat = dialog.findViewById<TextView>(R.id.error_flat)
            errorFlat.text=""

            if (checkCityAndStreet(cityNew, errorCity) && checkCityAndStreet(
                    streetNew,
                    errorStreet
                ) && checkHouseAndFlat(houseNew, errorHouse) && checkHouseAndFlat(
                    flatNew,
                    errorFlat
                )
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    addressApi.addAddress(
                        "Bearer " + token,
                        Address(
                            0,
                            cityNew.text.toString(),
                            streetNew.text.toString(),
                            houseNew.text.toString(),
                            flatNew.text.toString()
                        )
                    )
                }
                finish()
                startActivity(intent)
            }
        }

    }

    fun checkCityAndStreet(editText: EditText, error: TextView): Boolean {
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(editText.text.toString())) {
            editText.text.clear()
            error.text = "Може складатись лише з літер"
            return false
        }
        return true
    }

    fun checkHouseAndFlat(editText: EditText, error: TextView): Boolean {
        val regex = Regex("[0-9]+")
        if (!regex.matches(editText.text.toString())) {
            editText.text.clear()
            error.text = "Може складатись лише з цифр"
            return false
        }
        return true
    }
}