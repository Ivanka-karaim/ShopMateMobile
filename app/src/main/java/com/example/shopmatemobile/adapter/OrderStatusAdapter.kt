package com.example.shopmatemobile.adapter


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.databinding.ItemOrderStatusBinding
import com.example.shopmatemobile.model.Status


class OrderStatusAdapter(var context: Context):
    ListAdapter<Status, OrderStatusAdapter.ViewHolder>(CheckboxItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_status, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemOrderStatusBinding.bind(view)

        fun bind(item: Status) = with(binding){
            orderStatus.text = item.title
            imageStatusIcon.setImageResource(item.image.toInt())

            /*binding.executePendingBindings()*/
        }
    }
    class CheckboxItemDiffCallback : DiffUtil.ItemCallback<Status>() {
        override fun areItemsTheSame(oldItem: Status, newItem: Status): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Status, newItem: Status): Boolean {
            return oldItem == newItem
        }
    }
}


