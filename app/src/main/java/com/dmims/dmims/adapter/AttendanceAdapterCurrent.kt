package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.dataclass.AttendanceStud
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.AttendanceStudCurrent

class AttendanceAdapterCurrent(val userlist: ArrayList<AttendanceStudCurrent>) :
    RecyclerView.Adapter<AttendanceAdapterCurrent.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.attendance_adaptercurrent, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val attStud: AttendanceStudCurrent = userlist[p1]
        p0.txtDEPT_NAME?.text = attStud.DEPT_NAME
        p0.txtDEPT_ID?.text = attStud.DEPT_ID
        p0.txtTHEORY?.text = attStud.THEORY
        p0.txtPRACTICAL?.text = attStud.PRACTICAL
        p0.txtCLINICAL?.text = attStud.CLINICAL
        p0.txtPRACTICAL_ABSENT?.text = attStud.PRACTICAL_ABSENT
        p0.txtNO_LECTURER?.text = attStud.NO_LECTURER
        p0.txtCLINICAL_ABSENT?.text = attStud.CLINICAL_ABSENT
        p0.txtTHEORY_ABSENT?.text = attStud.THEORY_ABSENT
        p0.txtPERCENTAGE?.text = attStud.PERCENTAGE
        p0.imageViews?.setImageResource(attStud.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtDEPT_NAME = itemView.findViewById<TextView>(R.id.txtDEPT_NAME)
        val txtDEPT_ID = itemView.findViewById<TextView>(R.id.txtDEPT_ID)
        val txtTHEORY = itemView.findViewById<TextView>(R.id.txtTHEORY)
        val txtPRACTICAL = itemView.findViewById<TextView>(R.id.txtPRACTICAL)
        val txtCLINICAL = itemView.findViewById<TextView>(R.id.txtCLINICAL)
        val txtPRACTICAL_ABSENT = itemView.findViewById<TextView>(R.id.txtPRACTICAL_ABSENT)
        val txtNO_LECTURER = itemView.findViewById<TextView>(R.id.txtNO_LECTURER)
        val txtCLINICAL_ABSENT = itemView.findViewById<TextView>(R.id.txtCLINICAL_ABSENT)
        val txtTHEORY_ABSENT = itemView.findViewById<TextView>(R.id.txtTHEORY_ABSENT)
        val txtPERCENTAGE = itemView.findViewById<TextView>(R.id.txtPERCENTAGE)
        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}