package com.example.nsl_app.adapters

import android.content.Context
import android.icu.text.DateTimePatternGenerator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.models.RepoCardItem

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

            // 카드의 가로 크기를 전체 화면 크기의 70%로 한정.
            // 이는 카드 가로를 일부로 어느정도 남겨 다음 카드가 보이게 하기 위함.
            // 다음 카드가 보이면 옆으로 슬라이드 할 수 있다는 것을 시각적으로 보다 직관적이게 알 수 있음.
            cv_repo_card!!.layoutParams.width = displayWidth / 100 * 85


            var tags = ""
            for (i in repoItem[position].langTag) {
                tags += "$i |"
            }

            tags = tags.trimEnd { it == '|' }
            tags = tags.trimEnd()
            tv_item_repo_tag!!.text = tags
        }
    }

    override fun getItemCount(): Int {
        return repoItem.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cv_repo_card = itemView.findViewById<CardView>(R.id.cv_repo_card)
        var tv_item_repo_name = itemView.findViewById<TextView>(R.id.tv_item_repo_name)
        var tv_item_repo_tag = itemView.findViewById<TextView>(R.id.tv_item_repo_tag)
        var tv_item_repo_description =
            itemView.findViewById<TextView>(R.id.tv_item_repo_description)
    }
}