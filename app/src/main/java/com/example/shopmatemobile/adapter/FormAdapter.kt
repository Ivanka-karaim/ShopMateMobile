package com.example.shopmatemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R


class FormAdapter(private val dataList: List<Pair<String, String>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_FORM = 1
    private val VIEW_TYPE_BUTTON = 2

    override fun getItemViewType(position: Int): Int {
        // Логіка визначення типу елемента залежно від позиції
        return if (position < dataList.size) VIEW_TYPE_FORM else VIEW_TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FORM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false)
                FormViewHolder(view)
            }
            VIEW_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
                ButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FormViewHolder) {
            val (label, hint) = dataList[position]
            holder.textLabel.text = label
            holder.editField.hint = hint
        } else if (holder is ButtonViewHolder) {
            // Додайте логіку для елемента з кнопкою
            holder.button.setOnClickListener {
                // Обробка події кліку на кнопку
            }
        }
    }

    override fun getItemCount(): Int {
        // Кількість елементів дорівнює довжині списку даних + 1 для кнопки
        return dataList.size + 1
    }

    // ViewHolder для елемента з текстом та формою
    class FormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textLabel: TextView = itemView.findViewById(R.id.textLabel)
        val editField: EditText = itemView.findViewById(R.id.editField)
    }

    // ViewHolder для елемента з кнопкою
    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button)
    }
}

