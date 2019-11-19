package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.dataclass.AttendanceStud
import com.dmims.dmims.R

class AttendanceAdapter(val userlist: ArrayList<AttendanceStud>) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.attendance_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val attStud: AttendanceStud = userlist[p1]
        p0.txtdept?.text = attStud.dept
        p0.txtdeptid?.text = attStud.deptid
        p0.txtdate?.text = attStud.date
        p0.txtstatus?.text = attStud.status
        p0.imageViews?.setImageResource(attStud.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtdept = itemView.findViewById<TextView>(R.id.txtdept)
        val txtdeptid = itemView.findViewById<TextView>(R.id.txtdeptid)
        val txtdate = itemView.findViewById<TextView>(R.id.txtdate)
        val txtstatus = itemView.findViewById<TextView>(R.id.txtstatus)
        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}