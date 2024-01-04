package com.example.shopmatemobile.addResources

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.shopmatemobile.SignIn

object ErrorHandler {
    fun unauthorizedUser(context: Context, activity: Activity){
        SharedPreferencesFactory(context).clearToken()
        activity.finishAffinity()
        val intent = Intent(context, SignIn::class.java)
        context.startActivity(intent)
    }
    fun generalError(context: Context){
        Toast.makeText(
            context,
            "Уппс! Пішло щось не так",
            Toast.LENGTH_SHORT
        ).show()

    }
}