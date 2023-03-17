package com.example.nsl_app.adapters

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R

class BookRegisterImageAdapter(val context: Context, private val imageUris: ArrayList<Uri>) :
    RecyclerView.Adapter<BookRegisterImageAdapter.Holder>() {

    interface OnImageRemoveClickListener { fun onRemove(position: Int) }
    lateinit var onImageRemoveClickListener: OnImageRemoveClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_book_register_image, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUris[position])
            iv_item_book_image.setImageBitmap(bitmap)
            iv_item_book_image.clipToOutline = true
            iv_item_book_remove.setOnClickListener {
                onImageRemoveClickListener.onRemove(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_item_book_image = itemView.findViewById<ImageView>(R.id.iv_item_book_image)
        var iv_item_book_remove = itemView.findViewById<ImageView>(R.id.iv_item_book_remove)
    }
}