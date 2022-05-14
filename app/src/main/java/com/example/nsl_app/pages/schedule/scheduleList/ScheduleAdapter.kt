package com.example.nsl_app.pages.schedule.scheduleList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.pages.schedule.ScheduleFragment
import java.text.SimpleDateFormat

class ScheduleAdapter (val context: Context, private val scheduleItemList: ArrayList<ScheduleFragment.ScheduleData>) :
    RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    val dateFormat = SimpleDateFormat("M월 d일")
    val dateTimeFormat = SimpleDateFormat("M월 d일\na h:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_schedule, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {

            val scheduleItem = scheduleItemList[position]

            tv_item_date_title.text = scheduleItem.title

            tv_item_date_start.text = if(scheduleItem.startIsIncludeTime) {
                dateTimeFormat.format(scheduleItem.startDate!!.time)
            } else {
                dateFormat.format(scheduleItem.startDate!!.time)
            }

            if(scheduleItem.endDate == null) {
                tv_item_date_end.visibility = View.GONE
            } else {
                tv_item_date_end.visibility = View.VISIBLE

                if(scheduleItem.endIsIncludeTime) {
                    tv_item_date_end.text = dateTimeFormat.format(scheduleItem.endDate!!.time)
                } else {
                    tv_item_date_end.text = dateFormat.format(scheduleItem.endDate!!.time)
                }
            }
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