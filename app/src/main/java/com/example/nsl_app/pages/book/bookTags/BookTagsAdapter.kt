package com.example.nsl_app.pages.book.bookTags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R

class BookTagsAdapter (val context: Context, private val bookTags : ArrayList<String>) :
    RecyclerView.Adapter<BookTagsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_book_tag, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            tv_item_book_tag!!.text = "#${bookTags[position]}"
        }
    }

    override fun getItemCount(): Int {
        return bookTags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_book_tag = itemView.findViewById<TextView>(R.id.tv_item_book_tag)
    }
}