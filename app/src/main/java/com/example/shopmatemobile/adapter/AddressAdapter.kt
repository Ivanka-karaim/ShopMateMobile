package com.example.shopmatemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.AddressClickListener
import com.example.shopmatemobile.addResources.ButtonClickListener
import com.example.shopmatemobile.databinding.ItemAddressBinding
import com.example.shopmatemobile.model.Address

class AddressAdapter(var context: Context, var addressClickListener: AddressClickListener): ListAdapter<Address, AddressAdapter.Holder>(AddressAdapter.Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemAddressBinding.bind(view)

        fun bind(address: Address, context: Context, addressClickListener: AddressClickListener)= with(binding){
            city.text = address.city
            street.text = address.street
            house.text = address.house
            flat.text = address.flat
            deleteAddress.setOnClickListener {
                addressClickListener.deleteAddress(address.id)
            }

            editAddresss.setOnClickListener {
                addressClickListener.editAddress(address.id, city.text.toString(), street.text.toString(), house.text.toString(), flat.text.toString())
            }
        }
    }
    class Comparator: DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.city == newItem.city
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: AddressAdapter.Holder, position: Int) {
        holder.bind(getItem(position), context, addressClickListener)
    }

}