package com.example.shopmatemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopmatemobile.R
import com.example.shopmatemobile.databinding.ItemOrderProductBinding
import com.example.shopmatemobile.model.OrderProduct

class OrderProductAdapter(var context: Context): ListAdapter<OrderProduct, OrderProductAdapter.Holder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemOrderProductBinding.bind(view)

        fun bind(item: OrderProduct, context: Context) = with(binding){
            productName.text = item.title
            productPrice.text = item.price.toString()
            textCount.text = item.count.toString()
            Glide.with(context)
                .load(item.thumbnail)
                .into(productImage)
            binding.executePendingBindings()
        }
    }
    class Comparator : DiffUtil.ItemCallback<OrderProduct>() {
        override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem == newItem
        }
    }
}

