
package com.example.shopmatemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.model.BasketCheckbox
import com.example.shopmatemobile.databinding.ItemCheckboxBinding


class CheckboxAdapter: ListAdapter<BasketCheckbox, CheckboxAdapter.ViewHolder>(CheckboxItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCheckboxBinding.bind(view)

        fun bind(item: BasketCheckbox) = with(binding){
            productName.text = item.title
            productRate.text = item.grade.toString()
            productPrice.text = item.price.toString()
            textCount.text = item.count.toString()
            binding.executePendingBindings()
        }
    }
}

class CheckboxItemDiffCallback : DiffUtil.ItemCallback<BasketCheckbox>() {
    override fun areItemsTheSame(oldItem: BasketCheckbox, newItem: BasketCheckbox): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BasketCheckbox, newItem: BasketCheckbox): Boolean {
        return oldItem == newItem
    }
}

