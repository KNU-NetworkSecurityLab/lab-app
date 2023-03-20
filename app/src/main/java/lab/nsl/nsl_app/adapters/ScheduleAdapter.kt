package lab.nsl.nsl_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.pages.schedule.ScheduleFragment
import java.text.SimpleDateFormat

class ScheduleAdapter (val context: Context, val scheduleItemList: ArrayList<ScheduleFragment.ScheduleData>) :
    RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    interface ScheduleListener { fun onMoreClick(view: View, scheduleData: ScheduleFragment.ScheduleData) }

    lateinit var scheduleListener: ScheduleListener
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
        val btn_item_sch_option = itemView.findViewById<ImageButton>(R.id.btn_item_sch_option)

        init {
            btn_item_sch_option.setOnClickListener {
                scheduleListener.onMoreClick(it, scheduleItemList[adapterPosition])
            }
        }
    }
}