package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.EmUser
import com.dmims.dmims.dataclass.ListScheduledFeedback

class ScheduledFeedbackAdapter(val userlist:ArrayList<ListScheduledFeedback>):
    RecyclerView.Adapter<ScheduledFeedbackAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ScheduledFeedbackAdapter.ViewHolder {
        val view=LayoutInflater.from(p0.context).inflate(R.layout.card_scheduledfeedback_layout,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ScheduledFeedbackAdapter.ViewHolder, p1: Int) {
        val emUser: ListScheduledFeedback = userlist[p1]
        p0.tv_TitleFeedback?.text = emUser.title
        p0.tv_scheduledDate?.text = emUser.scheduledate
        p0.tv_startDate?.text = emUser.startdate
        p0.tv_endDate?.text = emUser.enddate

    }
    class ViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val tv_TitleFeedback= itemView.findViewById<TextView>(R.id.tv_TitleFeedback)
        val tv_scheduledDate = itemView.findViewById<TextView>(R.id.tv_scheduledate)
        val tv_startDate = itemView.findViewById<TextView>(R.id.tv_startdate)
        val tv_endDate = itemView.findViewById<TextView>(R.id.tv_enddate)


        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }
}