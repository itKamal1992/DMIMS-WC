package com.dmims.dmims.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.common.SaveImageHelper
import com.dmims.dmims.common.SaveImageHelpers
import com.dmims.dmims.dataclass.ListScheduledFeedback
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.dataclass.StudentGrievanceGet
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.*
import java.util.*
import kotlin.collections.ArrayList

class GrievanceAdapterStudShow(val userlist: ArrayList<StudentGrievanceGet>, context: Context) :
    RecyclerView.Adapter<GrievanceAdapterStudShow.ViewHolder>(), View.OnClickListener {
    lateinit var context:Context
    override fun onClick(v: View?) {
        v!!.id
        println("view >>> "+v!!.id)
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
init {
    this.context = context
}
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GrievanceAdapterStudShow.ViewHolder {
        val view=LayoutInflater.from(p0.context).inflate(R.layout.card_studsubmitgriv_layout,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val emUser: StudentGrievanceGet = userlist[p1]

        p0.camera_image.setImageResource(emUser.image)
        p0.tv_Date?.text = emUser.G_DATE
        p0.tv_GrievanceId?.text = emUser.G_ID
        p0.tv_ComplaintTo?.text = emUser.Comp_To
        p0.tv_subject?.text = emUser.G_SUBJECT
        p0.category?.text =emUser.G_CATEGORY
        p0.complaintAgainst?.text = emUser.G_AGAINST
        if (emUser.G_STATUS.equals("Open"))
        {
            p0.grievStatus?.text=emUser.G_STATUS
            p0.grievStatus.setTextColor(Color.parseColor("#00FF00"))
        }else
        {
            p0.grievStatus?.text=emUser.G_STATUS
            p0.grievStatus.setTextColor(Color.parseColor("#FF0000"))
        }
        p0.grivDescription.text=emUser.G_DISCRIPTION


        p0.ll_More.visibility=View.GONE
        p0.moreContent.setOnClickListener {

            p0.ll_More.visibility=View.VISIBLE
            p0.moreContent.visibility=View.GONE
            println("more content")
        }
        p0.linearForPdfClick.setOnClickListener {
            if ((emUser!!.G_ATTACHMENT == "F")||!((emUser!!.ATTACHMENT_URL.contains(".jpg",ignoreCase = true))||(emUser!!.ATTACHMENT_URL.contains(".jpeg",ignoreCase = true))||(emUser!!.ATTACHMENT_URL.contains(".png",ignoreCase = true))||(emUser!!.ATTACHMENT_URL.contains(".pdf",ignoreCase = true)))) {
                Toast.makeText(context, "NO Attachment for this notice", Toast.LENGTH_SHORT).show()
            }else
            if ((emUser!!.ATTACHMENT_URL.contains(".jpg",ignoreCase = true))||(emUser!!.ATTACHMENT_URL.contains(".jpeg",ignoreCase = true))||(emUser!!.ATTACHMENT_URL.contains(".png",ignoreCase = true)))
            {
                var intent: Intent = Intent(context, Common_Image_Viewer::class.java)
                intent.putExtra("url", emUser!!.ATTACHMENT_URL)
                intent.putExtra("actionTitle", "Image Viewer")
                context.startActivity(intent)
            }
            else //Code for PDF
                if (emUser!!.ATTACHMENT_URL.contains(".pdf",ignoreCase = true))
                {
                    var intent: Intent = Intent(context, Common_PDF_Viewer::class.java)
                    intent.putExtra("url",emUser!!.ATTACHMENT_URL)
                    intent.putExtra("actionTitle", "PDF Viewer")
                    context.startActivity(intent)
                }
        }

        p0.lessContent.setOnClickListener {

            p0.ll_More.visibility=View.GONE
            p0.moreContent.visibility=View.VISIBLE

            println("more content")
        }

        if (emUser.G_COMMENT.equals(""))
        {
            p0.ll_comment.visibility=View.GONE
        }else
        {
            p0.ll_comment.visibility=View.VISIBLE
        }


    }
    class ViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val tv_Date= itemView.findViewById<TextView>(R.id.tv_date)
        val tv_GrievanceId = itemView.findViewById<TextView>(R.id.tv_GrievanceId)
        val tv_ComplaintTo = itemView.findViewById<TextView>(R.id.tv_complaintTo)
        val tv_subject = itemView.findViewById<TextView>(R.id.tv_subject)
        val category = itemView.findViewById<TextView>(R.id.tv_category)
        val complaintAgainst = itemView.findViewById<TextView>(R.id.tv_complaintAgainst)
        val grievStatus=itemView.findViewById<TextView>(R.id.tv_GrievanceStatus)
        val grivDescription=itemView.findViewById<TextView>(R.id.tv_description)

        val moreContent=itemView.findViewById<ImageView>(R.id.iv_moreContent)
        val lessContent=itemView.findViewById<ImageView>(R.id.iv_less_Content)
        val ll_More=itemView.findViewById<LinearLayout>(R.id.ll_moreContent)
        val ll_comment=itemView.findViewById<LinearLayout>(R.id.ll_comment)
        val camera_image=itemView.findViewById<ImageView>(R.id.camera_image)
        val linearForPdfClick=itemView.findViewById<LinearLayout>(R.id.linearForPdfClick)

    }
}