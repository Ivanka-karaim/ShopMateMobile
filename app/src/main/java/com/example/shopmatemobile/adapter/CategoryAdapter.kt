package com.example.shopmatemobile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.ProductActivity
import com.example.shopmatemobile.R
import com.example.shopmatemobile.databinding.ItemCategoryBinding

class CategoryAdapter(var context: Context) : ListAdapter<String,CategoryAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemCategoryBinding.bind(view)

        fun bind(category: String, context: Context)= with(binding){
            buttonCategory.text = category
            buttonCategory.setOnClickListener {
                val intent = Intent(context, ProductActivity::class.java)

                intent.putExtra("NAME", category)
                context.startActivity(intent)
            }
        }
    }
    class Comparator: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), context)
    }
}