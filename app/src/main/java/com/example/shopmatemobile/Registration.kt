package com.example.shopmatemobile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.databinding.ActivityRegistrationBinding
import com.example.shopmatemobile.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Period
import java.time.format.DateTimeFormatter

class Registration : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.underlinedButton.setOnClickListener {
            val intent = Intent(this@Registration, SignIn::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            clean()
            val checkName = checkName()
            val checkSurname = checkSurname()
            val checkEmail = checkEmail()
            val checkPhoneNumber = checkPhoneNumber()
            val checkDateBirth = checkDateBirth()
            val checkPasswords = checkPasswords()
            if (checkName && checkSurname && checkEmail && checkDateBirth && checkPasswords && checkPhoneNumber){

                    val user = User(
                        binding.editName.text.toString(),
                        binding.editSurname.text.toString(),
                        binding.editEmail.text.toString(),
                        binding.editPassword1.text.toString(),
                        binding.editDateBirth.text.toString(),
                        binding.editPhoneNumber.text.toString()
                    )

                    val userApi = RetrofitClient.getInstance().create(UserApi::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = userApi.signUp(user)
                        if (response.isSuccessful) {
                            val token = response.body()
                            SharedPreferencesFactory(this@Registration).saveToken(token!!.token)
                            val intent = Intent(this@Registration, MainActivity2::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            val error = response.errorBody()?.string()
                            if (error.toString().contains("UserAlreadyExist")) {
                                runOnUiThread {
                                    binding.errorRegistration.text =
                                        "На дану пошту вже зареєстровано користувача"
                                }
                            }
                        }

                    }

            }

        }

    }
    fun togglePasswordVisibility(view: View) {
        val editText = findViewById<EditText>(R.id.editPassword1)
        val currentInputType = editText.inputType
        println(currentInputType)
        if (currentInputType ==144) {
            println(888)
            binding.hidden1.setImageResource(R.drawable.baseline_key_off_24)
            editText.inputType = 129
        } else {
            // Пароль прихований, переключаємо на видимий
            binding.hidden1.setImageResource(R.drawable.baseline_key_24)
            editText.inputType = 144
        }
        editText.setSelection(editText.text.length)
    }
    fun togglePassword2Visibility(view: View) {
        val editText = findViewById<EditText>(R.id.editPassword2)
        val currentInputType = editText.inputType
        println(currentInputType)
        if (currentInputType ==144) {
            println(888)
            binding.hidden2.setImageResource(R.drawable.baseline_key_off_24)
            editText.inputType = 129
        } else {
            // Пароль прихований, переключаємо на видимий
            binding.hidden2.setImageResource(R.drawable.baseline_key_24)
            editText.inputType = 144
        }
        editText.setSelection(editText.text.length)
    }



    fun clean(){
        binding.errorName.text=""
        binding.errorSurname.text=""
        binding.errorEmail.text=""
        binding.errorPhoneNumber.text=""
        binding.errorDateBirth.text=""
        binding.errorPassword1.text=""
        binding.errorPassword2.text=""
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

    fun checkEmail(): Boolean {
        val emailText = binding.editEmail.text.toString().trim()
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        if (!regex.matches(emailText)) {
            binding.editEmail.text.clear()
            binding.errorEmail.text = "Такої пошти не існує"
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDateBirth(): Boolean {
        try {
            val dateBirthText = binding.editDateBirth.text.toString().trim()

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy") // Формат дати
            val dateOfBirth =
                java.time.LocalDate.parse(dateBirthText, formatter) // Парсинг дати з рядка
            val currentDate = java.time.LocalDate.now() // Поточна дата
            val age = Period.between(dateOfBirth, currentDate).years
            if (age < 12) {
                binding.editDateBirth.text.clear()
                binding.errorDateBirth.text = "Вам менше 12 років. Реєстрація не можлива"
                return false
            }
        }catch (e:Exception){
            binding.editDateBirth.text.clear()
            binding.errorDateBirth.text = "Незрозумілий формат дати"
            return false
        }

        return true
    }

    fun checkPasswords(): Boolean {
        val uppercaseRegex = Regex("[A-Z]")
        val lowercaseRegex = Regex("[a-z]")
        val digitRegex = Regex("[0-9]")
        val password1 = binding.editPassword1.text.toString().trim()
        val password2 = binding.editPassword2.text.toString().trim()
        if (!uppercaseRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 велика літера"
            return false
        } else if (!lowercaseRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 маленька літера"
            return false
        } else if (!digitRegex.containsMatchIn(password1)) {
            binding.editPassword1.text.clear()
            binding.editPassword2.text.clear()
            binding.errorPassword1.text = "Має бути хоча б 1 цифра"
            return false
        } else if (password1 != password2) {
            binding.editPassword2.text.clear()
            binding.errorPassword2.text = "Паролі не збігаються"
            return false

        }
        return true
    }

}