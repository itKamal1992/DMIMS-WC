package com.dmims.dmims.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.AttendanceStudCurrent
import com.dmims.dmims.dataclass.GreivanceCellData

class GreivanceAdapterCurrent(val userlist: ArrayList<GreivanceCellData>) :
    RecyclerView.Adapter<GreivanceAdapterCurrent.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.greivance_adaptercurrent, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val attStud: GreivanceCellData = userlist[p1]
        p0.txtNAME_GRIE?.text = attStud.NAME_GRIE
        p0.txtJOB_TITLE?.text = attStud.JOB_TITLE
        p0.txtCOURSE_INSTITUTE?.text = attStud.COURSE_INSTITUTE
        p0.txtCOURSE_NAME?.text = attStud.COURSE_NAME
        p0.txtPHONE_NO?.text = attStud.PHONE_NO
        p0.txtEMAIL_ID?.text = attStud.EMAIL_ID
        p0.txtADDRESS?.text = attStud.ADDRESS
        p0.txtTYPE_GRIE?.text = attStud.TYPE_GRIE
        p0.txtINSERT_DATE?.text = attStud.INSERT_DATE
        p0.txtDETAIL_DESC?.text = attStud.DETAIL_DESC

        p0.txtOTHER_DETAILS?.text = attStud.OTHER_DETAILS
        p0.txtOTHER_INFO?.text = attStud.OTHER_INFO
        p0.txtPRO_SOL_GRIE?.text = attStud.PRO_SOL_GRIE
        p0.imageViews?.setImageResource(attStud.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNAME_GRIE = itemView.findViewById<TextView>(R.id.txtNAME_GRIE)
        val txtJOB_TITLE = itemView.findViewById<TextView>(R.id.txtJOB_TITLE)
        val txtCOURSE_INSTITUTE = itemView.findViewById<TextView>(R.id.txtCOURSE_INSTITUTE)
        val txtCOURSE_NAME = itemView.findViewById<TextView>(R.id.txtCOURSE_NAME)
        val txtPHONE_NO = itemView.findViewById<TextView>(R.id.txtPHONE_NO)
        val txtEMAIL_ID = itemView.findViewById<TextView>(R.id.txtEMAIL_ID)
        val txtADDRESS = itemView.findViewById<TextView>(R.id.txtADDRESS)
        val txtTYPE_GRIE = itemView.findViewById<TextView>(R.id.txtTYPE_GRIE)
        val txtINSERT_DATE = itemView.findViewById<TextView>(R.id.txtINSERT_DATE)
        val txtDETAIL_DESC = itemView.findViewById<TextView>(R.id.txtDETAIL_DESC)

        val txtOTHER_DETAILS = itemView.findViewById<TextView>(R.id.txtOTHER_DETAILS)
        val txtOTHER_INFO = itemView.findViewById<TextView>(R.id.txtOTHER_INFO)
        val txtPRO_SOL_GRIE = itemView.findViewById<TextView>(R.id.txtPRO_SOL_GRIE)
        val imageViews = itemView.findViewById<ImageView>(R.id.camera_image)
    }

}