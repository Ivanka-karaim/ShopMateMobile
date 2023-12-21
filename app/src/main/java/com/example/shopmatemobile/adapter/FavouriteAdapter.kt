package com.example.shopmatemobile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopmatemobile.ProductActivity
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.databinding.ItemProductBinding
import com.example.shopmatemobile.model.ProductShopMate

class FavouriteAdapter(var context: Context) : ListAdapter<ProductShopMate, FavouriteAdapter.Holder>(Comparator()){
    class Holder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemProductBinding.bind(view)

        fun bind(product: ProductShopMate, context: Context)= with(binding){
            productName!!.text = product.title
            productPrice!!.text = product.price.toString()
            productGrade!!.text = product.grade.toString()
            Glide.with(context)
                .load(product.thumbnail)
                .into(productPhoto!!)


            productWithOutLike!!.setOnClickListener {

                val intent = Intent(context, ProductActivity::class.java)
                intent.putExtra("ID", product.id)
                context.startActivity(intent)
            }


        }
    }
    class Comparator: DiffUtil.ItemCallback<ProductShopMate>(){
        override fun areItemsTheSame(oldItem: ProductShopMate, newItem: ProductShopMate): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ProductShopMate, newItem: ProductShopMate): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), context)
    }

}
