/* Created by Umesh Gaidhane*/
package com.dmims.dmims.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.dataclass.GreivanceCellData
import com.dmims.dmims.dataclass.McqFields
import com.dmims.dmims.dataclass.NoticeStudCurrent
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.*
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.camera_image
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtNOTICE_DATE
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtNOTICE_TITLE
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtPara
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtRegards
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtUSER_ROLE
import kotlinx.android.synthetic.main.student_get_mcq_adapter_layout.view.*

class StudentGetMCQAdapter(val userlist: ArrayList<McqFields>, context: Context) :
    RecyclerView.Adapter<StudentGetMCQAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var mcqList: List<McqFields> = userlist

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.student_get_mcq_adapter_layout, p0, false)
        return ViewHolder(v, ctx!!, mcqList)
    }

    init {
        this.mcqList = userlist
        this.ctx = context
    }

    override fun getItemCount(): Int {
        return mcqList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {


        val cc = userlist[p1]
        p0.setData(cc, p1)
    }

    class ViewHolder(itemView: View, ctx: Context, contacts: List<McqFields>) : RecyclerView.ViewHolder(itemView) {

        var mcqFields: McqFields? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                println("FileUrl >> "+mcqFields!!.FileUrl)
                                if (!mcqFields!!.FileUrl.contains("http")) {
                    Toast.makeText(itemView.context, "NO Attachment for this notice", Toast.LENGTH_SHORT).show()
                }
                else {
                    // Code for JGP, PNG
                    if ((mcqFields!!.FileUrl.contains(".jpg",ignoreCase = true))||(mcqFields!!.FileUrl.contains(".jpeg",ignoreCase = true))||(mcqFields!!.FileUrl.contains(".png",ignoreCase = true)))
                    {
                        var intent: Intent = Intent(ctx, Common_Image_Viewer::class.java)
                        intent.putExtra(
                            "url", mcqFields!!.FileUrl
                        )
                        ctx.startActivity(intent)
                    }
                    else //Code for PDF
                        if (mcqFields!!.FileUrl.contains(".pdf",ignoreCase = true))
                        {
                            var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
                            intent.putExtra("url", mcqFields!!.FileUrl)
                            intent.putExtra("actionTitle", "PDF Viewer")
                            ctx.startActivity(intent)
                        }



                }

            }
        }

        fun setData(cc: McqFields?, position: Int) {
            itemView.txtNOTICE_TITLE?.text = "MCQ Answer Key "+cc!!.Institute
            itemView.txtNOTICE_DATE?.text = "Date : "+cc!!.StartDate
            itemView.txtUSER_ROLE?.text = cc!!.UserRole
            itemView.txtYear?.text = "For students in Year : "+cc!!.Year
            itemView.txtPara?.text = "Dear student,\nYou have received MCQ Answer Key from "+cc!!.UserDesig+ " "+cc!!.UserName+". You can view this answer key from "+cc!!.StartDate+" to " +cc!!.EndDate+"."+"\nHence, we request you to please view the answer key for your respective examination."
            itemView.txtRegards?.text ="Regards, \nDMIMS APP"
            itemView.camera_image?.setImageResource(cc.image)
            this.mcqFields = cc
            this.currentPosition = position
        }
    }

}