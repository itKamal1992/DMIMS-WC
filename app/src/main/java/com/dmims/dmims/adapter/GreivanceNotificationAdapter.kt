package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.GreivanceCellData

class GreivanceNotificationAdapter(val userlist: ArrayList<GreivanceCellData>) :
    RecyclerView.Adapter<GreivanceNotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.greivance_adapter_notification, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val attStud: GreivanceCellData = userlist[p1]
        p0.txtNAME_GRIE?.text = "Name : "+attStud.NAME_GRIE
        p0.txtINSERT_DATE?.text = "Date : "+attStud.INSERT_DATE
        p0.txtCOURSE_INSTITUTE?.text = "Institute : "+attStud.COURSE_INSTITUTE
        p0.txtPara?.text = "Dear admin,\nYou have received grievance from student name "+attStud.NAME_GRIE+", "+attStud.COURSE_INSTITUTE+", "+attStud.COURSE_NAME+" registered with phone no "+
                attStud.PHONE_NO+", email id "+attStud.EMAIL_ID+", "+attStud.ADDRESS+".\nHence we request you to please address the grievance as soon as possible."
        p0.txtRegards?.text = "Regards, \nDMIMS APP"
        p0.imageViews?.setImageResource(attStud.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNAME_GRIE = itemView.findViewById<TextView>(R.id.txtNAME_GRIE)
        val txtINSERT_DATE = itemView.findViewById<TextView>(R.id.txtINSERT_DATE)
        val txtCOURSE_INSTITUTE = itemView.findViewById<TextView>(R.id.txtCOURSE_INSTITUTE)
        val txtPara = itemView.findViewById<TextView>(R.id.txtPara)
        val txtRegards = itemView.findViewById<TextView>(R.id.txtRegards)


        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}