package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopmatemobile.api.ProductApi
import com.example.shopmatemobile.api.UserApi
import com.example.shopmatemobile.databinding.ActivityMain2Binding
import com.example.shopmatemobile.databinding.ActivitySignInBinding
import com.example.shopmatemobile.model.SignInModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MyTrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tv = binding.tv

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val trustManager: TrustManager = MyTrustManager()

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)



        val client = OkHttpClient.Builder()// Додати фабрику для довірених сертифікатів
            .sslSocketFactory(sslContext.socketFactory, trustManager as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://192.168.31.22:7019/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        val userApi = retrofit.create(UserApi::class.java)
//        val productApi = retrofit.create(ProductApi::class.java)
        binding.signInButton.setOnClickListener {
            println("OKBashmachok")
            CoroutineScope(Dispatchers.IO).launch {
                println("OKBashmachok2")
                try {
                    val token = userApi.signIn(
                        SignInModel(
                            binding.editEmail.text.toString(),
                            binding.editPassword1.text.toString()
                        )
                    )


                println("OKBashmachok3")
                runOnUiThread {
                    tv.text = token.token
                }
                }catch (e: Exception){
                    println(12341234)
                    println(e)
                }

            }
        }
    }
}