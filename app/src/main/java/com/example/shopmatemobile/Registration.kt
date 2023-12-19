package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.model.SignInModel
import com.example.shopmatemobile.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val dateBirth = java.util.Date(userDateBirth.text.toString())
            val password1 = userPassword1.text.toString().trim()
            val password2 = userPassword2.text.toString().trim()
            if(name =="" || surname =="" || phoneNumber=="" || email==""  || password1=="" || password2==""){
                Toast.makeText(this, "Не всі поля заповнені", Toast.LENGTH_LONG ).show()
            }else{
                val user = User(name, surname, email, password1, dateBirth, phoneNumber)
                val userApi = RetrofitClient.getInstance().create(UserApi::class.java)
                button.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val token = userApi.signUp(user)
                        SharedPreferencesFactory(this@Registration).saveToken(token.token)
                    }
                }
            }

        }
    }
}