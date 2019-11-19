package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.R
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.dataclass.GreivanceCellData
import com.dmims.dmims.dataclass.NoticeStudCurrent

class StudentNotificationAdapter(val userlist: ArrayList<NoticeStudCurrent>) :
    RecyclerView.Adapter<StudentNotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.student_adapter_notification, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val attStud: NoticeStudCurrent = userlist[p1]
        p0.txtNOTICE_TITLE?.text = "Title : "+attStud.NOTICE_TITLE
        p0.txtNOTICE_DATE?.text = "Date : "+attStud.NOTICE_DATE
        p0.txtUSER_ROLE?.text = "From : "+attStud.USER_ROLE
        p0.txtPara?.text = "Dear student,\nYou have received notice from "+attStud.USER_ROLE+", stating as "+attStud.NOTICE_DESC+".\nHence we request you to please address the notice as soon as possible."
        p0.txtRegards?.text = "Regards, \nDMIMS APP"
        p0.imageViews?.setImageResource(attStud.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNOTICE_TITLE = itemView.findViewById<TextView>(R.id.txtNOTICE_TITLE)
        val txtNOTICE_DATE = itemView.findViewById<TextView>(R.id.txtNOTICE_DATE)
        val txtUSER_ROLE = itemView.findViewById<TextView>(R.id.txtUSER_ROLE)
        val txtPara = itemView.findViewById<TextView>(R.id.txtPara)
        val txtRegards = itemView.findViewById<TextView>(R.id.txtRegards)


        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}