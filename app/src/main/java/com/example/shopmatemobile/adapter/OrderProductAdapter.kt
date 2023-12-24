package com.example.shopmatemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.databinding.ItemOrderProductBinding
import com.example.shopmatemobile.model.OrderProduct

class OrderProductAdapter: ListAdapter<OrderProduct, OrderProductAdapter.Holder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemOrderProductBinding.bind(view)

        fun bind(item: OrderProduct) = with(binding){
            productName.text = item.productName
            productRate.text = item.productRate.toString()
            productPrice.text = item.productPrice.toString()
            textCount.text = item.count.toString()
            binding.executePendingBindings()
        }
    }
    class Comparator : DiffUtil.ItemCallback<OrderProduct>() {
        override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem == newItem
        }
    }
}

