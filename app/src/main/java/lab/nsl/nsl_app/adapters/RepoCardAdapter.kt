package lab.nsl.nsl_app.adapters

import android.content.Context
import android.icu.text.DateTimePatternGenerator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.models.RepoCardItem

class RepoCardAdapter(
    val context: Context,
    private val displayWidth: Int,
    private val repoItem: ArrayList<RepoCardItem>
) :
    RecyclerView.Adapter<RepoCardAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_repo_card, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {

            tv_item_repo_name!!.text = repoItem[position].name
            tv_item_repo_description!!.text = repoItem[position].description

            var tags = ""
            for (i in repoItem[position].langTag) {
                tags += "$i |"
            }

            tags = tags.trimEnd { it == '|' }
            tags = tags.trimEnd()
            tv_item_repo_tag!!.text = tags

            if (tv_item_repo_tag.text.isEmpty() || tv_item_repo_tag.text == "" || tv_item_repo_tag.text == null) {
                tv_item_repo_tag!!.visibility = View.GONE
            } else {
                tv_item_repo_tag!!.visibility = View.VISIBLE
            }

            if (repoItem[position].description == "" || repoItem[position].description == null) {
                tv_item_repo_description!!.visibility = View.GONE
            } else {
                tv_item_repo_description!!.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return repoItem.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_repo_name = itemView.findViewById<TextView>(R.id.tv_item_repo_name)
        var tv_item_repo_tag = itemView.findViewById<TextView>(R.id.tv_item_repo_tag)
        var tv_item_repo_description =
            itemView.findViewById<TextView>(R.id.tv_item_repo_description)
    }
}