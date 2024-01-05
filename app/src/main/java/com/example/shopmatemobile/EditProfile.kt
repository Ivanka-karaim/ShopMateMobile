package com.example.shopmatemobile

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.ProfileApi
import com.example.shopmatemobile.databinding.ActivityEditProfileBinding
import com.example.shopmatemobile.model.EditProfile
import com.example.shopmatemobile.model.PasswordChange
import com.example.shopmatemobile.service.DateService.Companion.parseDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Period
import java.time.format.DateTimeFormatter


class EditProfile : AppCompatActivity() {
    lateinit var binding: ActivityEditProfileBinding
    private lateinit var profile: com.example.shopmatemobile.model.Profile
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Редагування профілю"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_brown_24) // Якщо потрібно змінити значок кнопки "назад"
            toolbar.setTitleTextColor(
                ContextCompat.getColor(
                    this@EditProfile,
                    R.color.dark_brown
                )
            )
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val profileApi = RetrofitClient.getInstance().create(ProfileApi::class.java)
        val token = SharedPreferencesFactory(this).getToken()!!
        val changeName = findViewById<View>(R.id.changeName) as EditText
        val changeSurname = findViewById<View>(R.id.changeSurname) as EditText
        val changePhoneNumber = findViewById<View>(R.id.changePhoneNumber) as EditText
        val changeEmail = findViewById<View>(R.id.changeEmail) as EditText
        val changeDateBirth = findViewById<View>(R.id.changeDateBirth) as EditText
        CoroutineScope(Dispatchers.IO).launch {
            profile = profileApi.getProfile("Bearer $token")
            changeName.setText(profile.firstName)
            changeSurname.setText(profile.lastName)
            changePhoneNumber.setText(profile.phoneNumber)
            changeEmail.setText(profile.email)
            changeDateBirth.setText(parseDate(profile.dateBirth))
        }


        val buttonPassword = findViewById<View>(R.id.underlinedButtonChangePassword) as Button
        buttonPassword.setOnClickListener { showPopupWindow(); }

        val buttonApplyChanges = findViewById<View>(R.id.buttonApplyChanges) as Button
        buttonApplyChanges.setOnClickListener{
            clean()
            val checkName = checkName()
            val checkSurname = checkSurname()
            val checkEmail = checkEmail()
            val checkPhoneNumber = checkPhoneNumber()
            val checkDateBirth = checkDateBirth()
            if (checkName && checkSurname && checkEmail && checkDateBirth && checkPhoneNumber){
                val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val formatterNeeded = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val dateOfBirth =
                    java.time.LocalDate.parse(binding.changeDateBirth.text, formatterDate)
                val dateOfBirthFormatted = dateOfBirth.format(formatterNeeded)
                val profile = EditProfile(
                    binding.changeName.text.toString(),
                    binding.changeSurname.text.toString(),
                    binding.changePhoneNumber.text.toString(),
                    binding.changeEmail.text.toString(),
                    dateOfBirthFormatted
                )

                CoroutineScope(Dispatchers.IO).launch {
                    profileApi.editProfile(profile, "Bearer $token")
                    val intent = Intent(this@EditProfile, MainActivity2::class.java)
                    startActivity(intent)

                }
            }
        }
    }
    private fun showPopupWindow(){

        val dialog = Dialog(this)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)

        dialog.setTitle(null)

        dialog.setContentView(R.layout.popup_layout)
        dialog.setCancelable(true)
        dialog.show()

        val changePasswordSubmit = dialog.findViewById<View>(R.id.button_change_password_submit)
        changePasswordSubmit.setOnClickListener{
            val profileApi = RetrofitClient.getInstance().create(ProfileApi::class.java)
            val token = SharedPreferencesFactory(this).getToken()!!

            val oldPassword = dialog.findViewById<View>(R.id.old_password) as EditText
            val newPassword = dialog.findViewById<View>(R.id.new_password) as EditText
            val newRepeatPassword = dialog.findViewById<View>(R.id.repeat_new_password) as EditText
            val errorPassword1 = dialog.findViewById<View>(R.id.errorPassword1) as TextView
            val errorPassword3 = dialog.findViewById<View>(R.id.errorPassword3) as TextView

            errorPassword1.text = ""
            errorPassword3.text = ""
            val uppercaseRegex = Regex("[A-Z]")
            val lowercaseRegex = Regex("[a-z]")
            val digitRegex = Regex("[0-9]")
            if (!uppercaseRegex.containsMatchIn(newPassword.text.toString())) {
                errorPassword3.text = "Має бути хоча б 1 велика літера"
            } else if (!lowercaseRegex.containsMatchIn(newPassword.text.toString())) {
                errorPassword3.text = "Має бути хоча б 1 маленька літера"
            } else if (!digitRegex.containsMatchIn(newPassword.text.toString())) {
                errorPassword3.text = "Має бути хоча б 1 цифра"
            }
            else if (newPassword.text.toString()==newRepeatPassword.text.toString()){
                CoroutineScope(Dispatchers.IO).launch {
                    val response = profileApi.changePassword(
                        PasswordChange(oldPassword.text.toString(), newPassword.text.toString()),
                        "Bearer $token")
                    if (response.isSuccessful){
                        dialog.dismiss()
                        val intent = Intent(this@EditProfile, MainActivity2::class.java)
                        startActivity(intent)
                    }else {
                        runOnUiThread {
                            binding.apply {
                                errorPassword1.text = "Неправильний пароль"
                            }
                        }
                    }
                }
            }else{
                errorPassword3.text = "Паролі не збігаються."
            }
        }
        val close = dialog.findViewById<View>(R.id.closeButtonPopup)
        close.setOnClickListener{
            dialog.dismiss()
        }
    }
    fun clean(){
        binding.errorChangeName.text=""
        binding.errorChangeSurname.text=""
        binding.errorChangeEmail.text=""
        binding.errorChangePhoneNumber.text=""
        binding.errorChangeDateBirth.text=""
    }

    fun checkName(): Boolean {
        println("inCheckName")
        val nameText = binding.changeName.text.toString().trim()
        println(binding.changeName.text)
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(nameText)) {
            binding.changeName.text.clear()
            binding.errorChangeName.text = "Ім'я може складатись лише з літер"
            return false
        }
        return true

    }

    fun checkSurname(): Boolean {
        val surnameText = binding.changeSurname.text.toString().trim()
        val regex = Regex("[a-zA-Zа-яА-ЯҐґЄ-ЇІіЇїЄє]+")
        if (!regex.matches(surnameText)) {
            binding.changeSurname.text.clear()
            binding.errorChangeSurname.text = "Прізвище може складатись лише з літер"
            return false
        }
        return true
    }

    fun checkPhoneNumber(): Boolean {
        val phoneNumberText = binding.changePhoneNumber.text.toString().trim()
        val regex =
            Regex("^\\+?\\d{1,3}[\\s-]?(\\(\\d{3}\\)|\\d{3})[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}\$")
        if (!regex.matches(phoneNumberText)) {
            binding.changePhoneNumber.text.clear()
            binding.errorChangePhoneNumber.text = "Такого номера телефону не існує"
            return false
        }
        return true
    }

    fun checkEmail(): Boolean {
        val emailText = binding.changeEmail.text.toString().trim()
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        if (!regex.matches(emailText)) {
            binding.changeEmail.text.clear()
            binding.errorChangeEmail.text = "Такої пошти не існує"
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDateBirth(): Boolean {
        try {
            val dateBirthText = binding.changeDateBirth.text.toString().trim()

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy") // Формат дати
            val dateOfBirth =
                java.time.LocalDate.parse(dateBirthText, formatter) // Парсинг дати з рядка
            val currentDate = java.time.LocalDate.now() // Поточна дата
            val age = Period.between(dateOfBirth, currentDate).years
            if (age < 12) {
                binding.changeDateBirth.text.clear()
                binding.errorChangeDateBirth.text = "Вам менше 12 років. Реєстрація не можлива"
                return false
            }
        }catch (e:Exception){
            binding.changeDateBirth.text.clear()
            binding.errorChangeDateBirth.text = "Незрозумілий формат дати"
            return false
        }

        return true
    }


}