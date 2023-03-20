package lab.nsl.nsl_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.databinding.ItemBackgroundTagBinding

class BackgroundTagAdapter(val context: Context, private val tags: ArrayList<String>) :
    RecyclerView.Adapter<lab.nsl.nsl_app.adapters.BackgroundTagAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundTagAdapter.Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_background_tag, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: BackgroundTagAdapter.Holder, position: Int) {
        holder.binding.run {
            tvItemTag.text = tags[position]
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBackgroundTagBinding.bind(itemView)
    }
}