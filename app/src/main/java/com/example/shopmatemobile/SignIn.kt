package com.example.shopmatemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.databinding.ActivityMain2Binding
import com.example.shopmatemobile.databinding.ActivitySignInBinding
import com.example.shopmatemobile.model.SignInModel
import com.example.shopmatemobile.service.UserService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userApi = RetrofitClient.getInstance().create(UserApi::class.java)
        binding.underlinedButton.setOnClickListener {
            val intent = Intent(this@SignIn, Registration::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding.signInButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val errors = UserService.signIn(
                    userApi,
                    binding.editEmail.text.toString(),
                    binding.editPassword1.text.toString(),
                    this@SignIn
                )
                if (errors.isEmpty()) {
                    val intent = Intent(this@SignIn, MainActivity2::class.java)
                    startActivity(intent)
                } else if (errors == "emailError") {
                    binding.textErrorPassword.text = ""
                    binding.textErrorEmail.text = "Не існує такого користувача"
                    binding.editEmail.text.clear()
                    binding.editPassword1.text.clear()
                } else if (errors == "passwordError") {
                    binding.textErrorEmail.text = ""
                    binding.textErrorPassword.text = "Неправильний пароль"
                    binding.editPassword1.text.clear()
                }


            }
        }
    }
}

