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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmatemobile.adapter.AddressAdapter
import com.example.shopmatemobile.addResources.AddressClickListener
import com.example.shopmatemobile.databinding.ActivityAddressBinding
import com.example.shopmatemobile.service.AddressService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddressActivity : AppCompatActivity(), AddressClickListener {
    lateinit var binding: ActivityAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Адреси"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_brown_24) // Якщо потрібно змінити значок кнопки "назад"
            toolbar.setTitleTextColor(
                ContextCompat.getColor(
                    this@AddressActivity,
                    R.color.dark_brown
                )
            )
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val recyclerView = binding.RecyclerViewAddresses
        recyclerView.layoutManager =
            LinearLayoutManager(this@AddressActivity, LinearLayoutManager.VERTICAL, false)
        val adapter = AddressAdapter(this@AddressActivity, this@AddressActivity)
        recyclerView.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addresses =
                    AddressService(this@AddressActivity, this@AddressActivity).getAddress()
                runOnUiThread {
                    println(addresses)
                    adapter.submitList(addresses)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        binding.addAddress.setOnClickListener {
            this.addAddress()
        }
    }


    override fun deleteAddress(id: Int) {
        AddressService(this, this).deleteAddress(id)
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
                ) && (flatNew.text.toString().isEmpty() || AddressService(
                    this,
                    this
                ).checkHouseAndFlat(
                    flatNew,
                    errorFlat
                ))
            ) {


                AddressService(this, this).editAddress(
                    id,
                    cityNew.text.toString(),
                    streetNew.text.toString(),
                    houseNew.text.toString(),
                    flatNew.text.toString()
                )
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
        if (!isFinishing) {
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
                ) && (flatNew.text.toString().isEmpty() || AddressService(
                    this,
                    this
                ).checkHouseAndFlat(
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
                finish()
                startActivity(intent)
            }
        }
    }


}