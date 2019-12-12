package com.dmims.dmims.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.UpdateScheduledExamFeedback
import com.dmims.dmims.dataclass.EmUser
import com.dmims.dmims.dataclass.ListScheduledFeedback

class ScheduledFeedbackAdapter(userlist: ArrayList<ListScheduledFeedback>, context: Context) :
    RecyclerView.Adapter<ScheduledFeedbackAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var userlist: List<ListScheduledFeedback> = userlist


    init {
        this.userlist = userlist
        this.ctx = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ScheduledFeedbackAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context)
            .inflate(R.layout.card_scheduledfeedback_layout, p0, false)
        return ViewHolder(view, ctx!!, userlist)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val emUser: ListScheduledFeedback = userlist[p1]
        p0.tv_TitleFeedback?.text = emUser.title
        p0.tv_scheduledDate?.text = emUser.scheduledate
        p0.tv_startDate?.text = emUser.startdate
        p0.tv_endDate?.text = emUser.enddate
        p0.tv_year?.text = emUser.YEAR
        p0.tv_course?.text = emUser.COURSE_NAME
        p0.tv_dept?.text = emUser.DEPT_NAME


    }

    class ViewHolder(itemView: View, ctx: Context, list: List<ListScheduledFeedback>) :
        RecyclerView.ViewHolder(itemView) {
        val tv_TitleFeedback = itemView.findViewById<TextView>(R.id.tv_TitleFeedback)
        val tv_scheduledDate = itemView.findViewById<TextView>(R.id.tv_scheduledate)
        val tv_startDate = itemView.findViewById<TextView>(R.id.tv_startdate)
        val tv_endDate = itemView.findViewById<TextView>(R.id.tv_enddate)
        val btn_editSchedule = itemView.findViewById<Button>(R.id.btn_edit_feedbackSchedule)
        val tv_year = itemView.findViewById<TextView>(R.id.tv_year)
        val tv_course = itemView.findViewById<TextView>(R.id.tv_course)
        val tv_dept = itemView.findViewById<TextView>(R.id.tv_dept)


        init {
            btn_editSchedule.setOnClickListener {

                var s = list.get(adapterPosition)
                var b = s.id

                var title = s.title
                var scheduledate = s.scheduledate
                var startdate = s.startdate
                var enddate = s.enddate
                var YEAR = s.YEAR
                var COURSE_NAME = s.COURSE_NAME
                var DEPT_NAME = s.DEPT_NAME
                println("b   " + b)
                var intent: Intent = Intent(ctx, UpdateScheduledExamFeedback::class.java)
                intent.putExtra("id", b)

                intent.putExtra("title", title)
                intent.putExtra("scheduledate", scheduledate)
                intent.putExtra("startdate", startdate)
                intent.putExtra("enddate", enddate)
                intent.putExtra("YEAR", YEAR)
                intent.putExtra("COURSE_NAME", COURSE_NAME)
                intent.putExtra("DEPT_NAME", DEPT_NAME)
                ctx.startActivity(intent)


            }


        }


    }
}