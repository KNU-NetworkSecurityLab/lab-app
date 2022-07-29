package com.example.nsl_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R

class SchTagAddAdapter(val context: Context, private val tags: ArrayList<String>) :
    RecyclerView.Adapter<SchTagAddAdapter.Holder>() {

    var tagAddClickListener: TagAddClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_sch_tag_add, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            tv_item_tag!!.text = tags[position]
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_tag: TextView? = null

        init {
            tv_item_tag = itemView.findViewById<TextView>(R.id.tv_item_tag)
            itemView.setOnClickListener {
                tagAddClickListener?.onTagClick(tags[adapterPosition])
            }
        }
    }
}