package com.example.nsl_app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ItemMemberBinding
import com.example.nsl_app.models.MemberItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MemberAdapter(val context: Context, private val members: ArrayList<MemberItem>) :
    RecyclerView.Adapter<MemberAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_member, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvItemMemberName.text = members[position].name
            tvItemMemberDegree.text = "학번 ${members[position].studentNumber} | ${members[position].position}"
            tvItemMemberEmail.text = members[position].email

            val skills = members[position].skills

            val skillAdapter = BackgroundTagAdapter(context, skills)
            rvItemMemberSkills.adapter = skillAdapter

            FlexboxLayoutManager(context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }.let {
                rvItemMemberSkills.layoutManager = it
            }

            //rvItemMemberSkills.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            skillAdapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMemberBinding.bind(itemView)
    }
}