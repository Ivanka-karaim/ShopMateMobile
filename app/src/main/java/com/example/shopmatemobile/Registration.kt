package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userName: EditText = findViewById(R.id.editName)
        val userSurname: EditText = findViewById(R.id.editSurname)
        val userPhoneNumber: EditText = findViewById(R.id.editPhoneNumber)
        val userEmail: EditText = findViewById(R.id.editEmail)
        val userDateBirth: EditText = findViewById(R.id.editDateBirth)
        val userPassword1: EditText = findViewById(R.id.editPassword1)
        val userPassword2: EditText = findViewById(R.id.editPassword2)
        val button: Button = findViewById(R.id.button)


        button.setOnClickListener {
            val name = userName.text.toString().trim()
            val surname = userSurname.text.toString().trim()
            val phoneNumber = userPhoneNumber.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val dateBirth = userDateBirth.text.toString().trim()
            val password1 = userPassword1.text.toString().trim()
            val password2 = userPassword2.text.toString().trim()

            if(name =="" || surname =="" || phoneNumber=="" || email=="" || dateBirth=="" || password1=="" || password2==""){
                Toast.makeText(this, "Не всі поля заповнені", Toast.LENGTH_LONG ).show()

            }else{

            }

        }

//        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
//        val data = listOf(
//            Pair("Ім'я", "Володимир"),
//            Pair("Прізвище", "Зеленський"),
//            Pair("Номер телефону", "+38 (000) 000 00 00"),
//            Pair("Електронна пошта", "aaaaaa@gmail.com"),
//            Pair("Дата народження", "00.00.0000"),
//            Pair("Пароль", "*************"),
//            Pair("Повторіть пароль", "*************")
//        )
//
//        val layoutManager = LinearLayoutManager(this)
//        val adapter = FormAdapter(data)
//
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
    }
}