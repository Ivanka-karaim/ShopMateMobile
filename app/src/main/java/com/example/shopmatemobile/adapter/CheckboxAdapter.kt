
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
import com.example.shopmatemobile.addResources.RemoveFromBasket
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.BasketApi
import com.example.shopmatemobile.databinding.ItemCheckboxBinding
import com.example.shopmatemobile.model.Basket
import com.example.shopmatemobile.model.OrderProduct
import com.example.shopmatemobile.model.ProductId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CheckboxAdapter(private val changedListener: CheckboxChangedListener, private val removeListener: RemoveFromBasket, var context: Context):
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
    fun removeItem(item: OrderProduct) {
        val position = currentList.indexOf(item)
        if (position != -1) {
            val currentList = currentList.toMutableList()
            currentList.remove(item)
            submitList(currentList.toList())
            notifyItemRemoved(position)
        }
    }


    fun getSelectedItems(): List<OrderProduct> {
        return currentSelectedItems.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, changedListener, removeListener, context)
    }

    class ViewHolder(view: View, private val adapter: CheckboxAdapter) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCheckboxBinding.bind(view)


        fun bind(item: OrderProduct, changedListener: CheckboxChangedListener, removeListener: RemoveFromBasket, context: Context) = with(binding){
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

                        item.count -= 1
                        (itemView.context as? Activity)?.runOnUiThread {
                            binding.apply {
                                textCount.text = item.count.toString()
                            }
                            changedListener.onCheckboxChanged()
                        }
                        basketApi.deleteFromBasket("Bearer $token", Basket(item.id, 1))
                    }
                }
            }
            buttonPlus.setOnClickListener{
                val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
                val token = SharedPreferencesFactory(itemView.context).getToken()!!
                CoroutineScope(Dispatchers.IO).launch {

                    item.count+=1
                    (itemView.context as? Activity)?.runOnUiThread {
                        binding.apply {
                            textCount.text = item.count.toString()
                        }
                        changedListener.onCheckboxChanged()
                    }
                    basketApi.addToBasket("Bearer $token", Basket(item.id, 1))
                }
            }
            buttonDelete.setOnClickListener{
                val basketApi = RetrofitClient.getInstance().create(BasketApi::class.java)
                val token = SharedPreferencesFactory(itemView.context).getToken()!!
                CoroutineScope(Dispatchers.IO).launch {
                    item.count = 0
                    removeCheckbox(item)
                    basketApi.removeFromBasket("Bearer $token", item.id)
                    (itemView.context as? Activity)?.runOnUiThread {
                        adapter.removeItem(item)
                        changedListener.onCheckboxChanged()
                        removeListener.onRemoveBasket(item.id)
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


    class CheckboxItemDiffCallback : DiffUtil.ItemCallback<OrderProduct>() {
        override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
            return oldItem == newItem
        }
    }

}

