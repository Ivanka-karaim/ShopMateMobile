
package com.example.shopmatemobile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shopmatemobile.OrderActivity
import com.example.shopmatemobile.databinding.ItemOrderInfoBinding
import com.example.shopmatemobile.databinding.ItemOrderProductBinding
import com.example.shopmatemobile.model.OrderInfoProducts
import com.example.shopmatemobile.model.OrderProduct

class OrderAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: List<OrderInfoProducts>
) : BaseExpandableListAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var groupBinding: ItemOrderInfoBinding
    private lateinit var itemBinding: ItemOrderProductBinding

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        val orderInfoProducts = dataList[listPosition]
        return orderInfoProducts.productsOrder[expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            itemBinding = ItemOrderProductBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.title = itemBinding.productName
            holder.photo = itemBinding.productImage
            holder.price = itemBinding.productPrice
            holder.count = itemBinding.textCount
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }
        val expandedListProduct = getChild(listPosition, expandedListPosition) as OrderProduct
        holder.title!!.text = expandedListProduct.title
        holder.price!!.text = expandedListProduct.price.toString()
        holder.photo?.let {
            Glide.with(context)
                .load(expandedListProduct.thumbnail)
                .into(it)
        }
        holder.count!!.text = expandedListProduct.count.toString()
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        val orderInfoProducts = dataList[listPosition]
        return orderInfoProducts.productsOrder.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: GroupViewHolder
        if (convertView == null) {
            groupBinding = ItemOrderInfoBinding.inflate(inflater)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.label = groupBinding.orderDate
            holder.cost = groupBinding.orderCost
            convertView.tag = holder
            holder.buttonMore = groupBinding.buttonMore
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        val orderInfoProducts = dataList[listPosition]
        holder.label!!.text = orderInfoProducts.date
        holder.cost!!.text = orderInfoProducts.totalPrice.toString()
        holder.buttonMore!!.setOnClickListener{
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("orderId", orderInfoProducts.orderId.toString())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    inner class ItemViewHolder {
        internal var title: TextView? = null
        internal var price: TextView? = null
        internal var photo: ImageView? = null
        internal var count: TextView? = null
    }

    inner class GroupViewHolder {
        internal var label: TextView? = null
        internal var cost: TextView? = null
        internal var buttonMore: Button? = null
    }
}

