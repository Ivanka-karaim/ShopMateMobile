package com.example.shopmatemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.databinding.ItemCouponBinding
import com.example.shopmatemobile.model.Coupon
import com.example.shopmatemobile.model.CouponStatus

class CouponsAdapter:ListAdapter<CouponStatus, CouponsAdapter.Holder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coupon, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCouponBinding.bind(view)

        fun bind(item: CouponStatus) = with(binding){
            discount.text = "-${item.coupon.discount.toInt()}%"
            dateExpiration.text = item.coupon.dateExpiration
            status.text = item.status
            binding.executePendingBindings()
        }
    }
}

class Comparator : DiffUtil.ItemCallback<CouponStatus>() {
    override fun areItemsTheSame(oldItem: CouponStatus, newItem: CouponStatus): Boolean {
        return oldItem.coupon.id == newItem.coupon.id
    }

    override fun areContentsTheSame(oldItem: CouponStatus, newItem: CouponStatus): Boolean {
        return oldItem == newItem
    }
}