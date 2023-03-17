package com.example.nsl_app.adapters

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ItemBookBinding
import com.example.nsl_app.databinding.ItemBookImageBinding

class BookImageAdapter(val context: Context, private val imageUris: ArrayList<Bitmap>) :
    RecyclerView.Adapter<BookImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_book_image, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            holder.binding.ivBookImage.setImageBitmap(imageUris[position])
        }
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBookImageBinding.bind(itemView)
    }
}