
package com.example.shopmatemobile.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.CheckboxChangedListener
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.BasketApi
import com.example.shopmatemobile.databinding.ItemCheckboxBinding
import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.OrderProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CheckboxAdapter(private val changedListener: CheckboxChangedListener, var context: Context):
    ListAdapter<OrderProduct, CheckboxAdapter.ViewHolder>(CheckboxItemDiffCallback()) {

    companion object {
        var currentSelectedItems: MutableList<OrderProduct> = ArrayList()

        fun addCheckbox(basketCheckbox: OrderProduct){
            if (basketCheckbox !in currentSelectedItems){
                currentSelectedItems.add(basketCheckbox)
            }

        }
        fun removeCheckbox(basketCheckbox: OrderProduct){
            if (basketCheckbox in currentSelectedItems){
                currentSelectedItems.remove(basketCheckbox)
            }
        }

    }
    fun getSelectedItems(): List<OrderProduct> {
        return currentSelectedItems.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, changedListener, context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCheckboxBinding.bind(view)

        fun bind(item: OrderProduct, changedListener: CheckboxChangedListener, context: Context) = with(binding){
            addCheckbox(item)
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    addCheckbox(item)
                }else{
                    removeCheckbox(item)
                }
                changedListener.onCheckboxChanged()
            }
            buttonMinus.setOnClickListener {
                val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
                val token = SharedPreferencesFactory(itemView.context).getToken()!!
                if (item.count > 1) {
                    CoroutineScope(Dispatchers.IO).launch {
                        basketApi.deleteFromBasket("Bearer $token", Basket(item.id, 1))
                        item.count -= 1
                        (itemView.context as? Activity)?.runOnUiThread {
                            binding.apply {
                                textCount.text = item.count.toString()
                            }
                            changedListener.onCheckboxChanged()
                        }
                    }
                }
            }
            buttonPlus.setOnClickListener{
                val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
                val token = SharedPreferencesFactory(itemView.context).getToken()!!
                CoroutineScope(Dispatchers.IO).launch {
                    basketApi.addToBasket("Bearer $token", Basket(item.id, 1))
                    item.count+=1
                    (itemView.context as? Activity)?.runOnUiThread {
                        binding.apply {
                            textCount.text = item.count.toString()
                        }
                        changedListener.onCheckboxChanged()
                    }
                }
            }

            productName.text = item.title
            productPrice.text = item.price.toString()
            textCount.text = item.count.toString()
            Glide.with(context)
                .load(item.thumbnail)
                .into(productImage)
            binding.executePendingBindings()
        }
    }
}
class CheckboxItemDiffCallback : DiffUtil.ItemCallback<OrderProduct>() {
    override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
        return oldItem == newItem
    }
}

