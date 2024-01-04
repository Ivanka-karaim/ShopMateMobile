package com.example.shopmatemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shopmatemobile.addResources.SharedPreferencesFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val token = SharedPreferencesFactory(this).getToken()?: ""


        if (token.isNotEmpty()) {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun startNewActivity(view: View){
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)

    }
    fun startNewActivitySignIn(view: View){
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)

    }
}