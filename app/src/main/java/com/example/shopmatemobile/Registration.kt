package com.example.shopmatemobile

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.databinding.ActivityRegistrationBinding
import com.example.shopmatemobile.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date

class Registration : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userName: EditText = binding.editName
        val userSurname: EditText = findViewById(R.id.editSurname)
        val userPhoneNumber: EditText = findViewById(R.id.editPhoneNumber)
        val userEmail: EditText = findViewById(R.id.editEmail)
        val userDateBirth: EditText = findViewById(R.id.editDateBirth)
        val userPassword1: EditText = findViewById(R.id.editPassword1)
        val userPassword2: EditText = findViewById(R.id.editPassword2)
        val button: Button = findViewById(R.id.button)
        val errorRegistration: TextView = findViewById(R.id.errorRegistration)


        button.setOnClickListener {
            val date = binding.editDateBirth.text.toString()
            println(date)





            val user = User(
                binding.editName.text.toString(),
                binding.editSurname.text.toString(),
                binding.editEmail.text.toString(),
                binding.editPassword1.text.toString(),
                date,
                binding.editPhoneNumber.text.toString()
            )

            val userApi = RetrofitClient.getInstance().create(UserApi::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = userApi.signUp(user)
                    if (response.isSuccessful) {
                        val token = response.body()
                        SharedPreferencesFactory(this@Registration).saveToken(token!!.token)
                    } else {
                        val error = response.errorBody()?.string()
                        if (error.toString().contains("UserAlreadyExist")) {
                            errorRegistration.text = "На дану пошту вже зареєстровано користувача"
                        }
                    }

                }

        }

    }

    fun checkName(): Boolean {
        val nameText = binding.editName.text.toString().trim()
        val regex = Regex("[a-zA-Z]+")
        if (!regex.matches(nameText)) {
            binding.editName.text.clear()
            binding.errorName.text = "Ім'я може складатись лише з літер"
        }
        return true

    }

    fun checkSurname(): Boolean {
        val surnameText = binding.editSurname.text.toString().trim()
        val regex = Regex("[a-zA-Z]+")
        if (!regex.matches(surnameText)) {
            binding.editSurname.text.clear()
            binding.errorSurname.text = "Прізвище може складатись лише з літер"
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
        }
        return true
    }

    fun checkEmail(): Boolean {
        val emailText = binding.editEmail.text.toString().trim()
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        if (regex.matches(emailText)) {
            binding.editEmail.text.clear()
            binding.errorEmail.text = "Такої пошти не існує"
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDateBirth(): Boolean {
        val dateBirthText = binding.editDateBirth.text.toString().trim()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy") // Формат дати
        val dateOfBirth =
            java.time.LocalDate.parse(dateBirthText, formatter) // Парсинг дати з рядка
        val currentDate = java.time.LocalDate.now() // Поточна дата
        val age = Period.between(dateOfBirth, currentDate).years
        if (age < 12) {
            binding.editDateBirth.text.clear()
            binding.errorDateBirth.text = "Вам менше 12 років. Реєстрація не можлива"
        }
        if (java.time.LocalDate.parse(dateBirthText, formatter) > java.time.LocalDate.now()) {
            binding.editDateBirth.text.clear()
            binding.errorDateBirth.text = "Не можливо"
        }
        return true
    }

    fun checkPasswords(): Boolean {
        val uppercaseRegex = Regex("[A-Z]")
        val lowercaseRegex = Regex("[a-z]")
        val digitRegex = Regex("[0-9]")
        val password1 = binding.editPassword1.text.toString().trim()
        val password2 = binding.editPassword2.text.toString().trim()
        if (password1 != password2) {
            binding.editPassword2.text.clear()
            binding.errorPassword2.text = "Паролі не збігаються"

        } else if (uppercaseRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 велика літера"
        } else if (lowercaseRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 маленька літера"
        } else if (digitRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 цифра"
        }
        return true
    }


    fun checkData(
        name: EditText,
        surname: EditText,
        phoneNumber: EditText,
        dateBirth: EditText,
        email: EditText,
        password1: EditText,
        password2: EditText
    ) {

    }
}