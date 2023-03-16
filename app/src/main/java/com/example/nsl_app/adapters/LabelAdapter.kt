package com.example.nsl_app.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ItemDefaultLabelBinding

class LabelAdapter(val context: Context, private val bookTags: ArrayList<String>) :
    RecyclerView.Adapter<LabelAdapter.Holder>() {

    var textColor: Int = Color.BLACK
    var textSize: Int = 14
    var startString = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_default_label, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvLabel.text = "${startString}${bookTags[position]}"

            tvLabel.setTextColor(textColor)
            tvLabel.textSize = textSize.toFloat()
        }
    }

    override fun getItemCount(): Int {
        return bookTags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDefaultLabelBinding.bind(itemView)
    }
}