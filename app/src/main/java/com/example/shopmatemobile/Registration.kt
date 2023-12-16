package com.example.shopmatemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.adapter.FormAdapter

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val data = listOf(
            Pair("Ім'я", "Володимир"),
            Pair("Прізвище", "Зеленський"),
            Pair("Номер телефону", "+38 (000) 000 00 00"),
            Pair("Електронна пошта", "aaaaaa@gmail.com"),
            Pair("Дата народження", "00.00.0000"),
            Pair("Пароль", "*************"),
            Pair("Повторіть пароль", "*************")
        )

        val layoutManager = LinearLayoutManager(this)
        val adapter = FormAdapter(data)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}