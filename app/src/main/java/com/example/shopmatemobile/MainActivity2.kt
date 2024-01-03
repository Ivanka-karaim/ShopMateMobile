package com.example.shopmatemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopmatemobile.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())


        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.basket -> replaceFragment(Basket())
                R.id.favourite -> replaceFragment(Favourite())
                R.id.profile -> replaceFragment(Profile())

                else -> {

                }

            }
            true

        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    fun startActivityEditProfile(view: View){
        val intent = Intent(this, EditProfile::class.java)
        startActivity(intent)
    }

    fun startActivityOrderCreation(view: View){
        val intent = Intent(this, OrderCreation::class.java)
        startActivity(intent)
    }

    fun startActivityCoupons(view: View){
        val intent = Intent(this, Coupons::class.java)
        startActivity(intent)
    }
}