package com.example.nsl_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R

class SchTagAdapter(val context: Context, private val selectedTags: ArrayList<String>) :
    RecyclerView.Adapter<SchTagAdapter.Holder>() {

    var tagRemoveClickListener: TagRemoveClickListener? = null

    interface TagRemoveClickListener {
        fun onRemoveClick(tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_removable_label, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            tv_item_tag!!.text = selectedTags[position]
        }
    }

    override fun getItemCount(): Int {
        return selectedTags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_tag: TextView? = null

        init {
            tv_item_tag = itemView.findViewById<TextView>(R.id.tv_item_tag)
            itemView.setOnClickListener {
                tagRemoveClickListener?.onRemoveClick(selectedTags[adapterPosition])
            }
        }
    }
}