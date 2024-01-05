package com.example.shopmatemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmatemobile.R
import com.example.shopmatemobile.addResources.ButtonClickListener

class RadioAdapter(private val data: List<Any>, private val buttonClickListener: ButtonClickListener, private val type: String) :
    RecyclerView.Adapter<RadioAdapter.RadioViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.radio_button_item_layout, parent, false)
        return RadioViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)

        init {
            radioButton.setOnClickListener {
                val prevSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(prevSelected)
                notifyItemChanged(selectedPosition)

                buttonClickListener.onButtonClick(radioButton.text.toString(), type)


            }
        }

        fun bind(position: Int) {
            radioButton.text = data[position].toString()
            radioButton.isChecked = selectedPosition == adapterPosition
        }
    }
}
