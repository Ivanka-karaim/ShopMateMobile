package com.example.shopmatemobile.addResources

import android.content.Intent
import com.example.shopmatemobile.SignIn

class UnauthorizedUser {
    private val instance: UnauthorizedUser = UnauthorizedUser()

   fun getInstance():UnauthorizedUser{
        return instance
    }
}