package com.example.nsl_app.pages.schedule.scheduleList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.pages.schedule.ScheduleFragment

class ScheduleAdapter (val context: Context, private val scheduleItemList: ArrayList<ScheduleFragment.ScheduleData>) :
    RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_schedule, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            tv_item_date_title.text = scheduleItemList[position].title
        }
    }

    override fun getItemCount(): Int {
        return scheduleItemList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_item_date_start = itemView.findViewById<TextView>(R.id.tv_item_sch_date_start)
        val tv_item_date_end = itemView.findViewById<TextView>(R.id.tv_item_sch_date_end)
        val tv_item_date_title = itemView.findViewById<TextView>(R.id.tv_item_sch_date_title)

    }
}