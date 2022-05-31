package com.example.nsl_app.pages.home.bookPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R

class BookAdapter (val context: Context, private val bookItem : ArrayList<BookItem>) :
    RecyclerView.Adapter<BookAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_book, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            tv_item_book_name!!.text = bookItem[position].bookName
            tv_item_book_author!!.text = bookItem[position].bookAuthor
        }
    }

    override fun getItemCount(): Int {
        return bookItem.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_book_name = itemView.findViewById<TextView>(R.id.tv_item_book_name)
        var tv_item_book_author = itemView.findViewById<TextView>(R.id.tv_item_book_author)
        var list_book_tag = itemView.findViewById<RecyclerView>(R.id.list_book_tag)
    }
}