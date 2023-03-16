package com.example.nsl_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ItemBackgroundTagBinding
import com.example.nsl_app.databinding.ItemRemovableLabelBinding

class RemovableLabelAdapter(val context: Context, private val tags: ArrayList<String>) :
    RecyclerView.Adapter<RemovableLabelAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_removable_label, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvItemTag.text = tags[position]
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemRemovableLabelBinding.bind(itemView)

        init {
            // 인터페이스를 분리할 수 있지만,
            // 액티비티에서 처리하는 것 보단 응집도를 위해 어댑터 내에서 처리 함
            binding.imgItemTagRemove.setOnClickListener {
                tags.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}