package com.dmims.dmims.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.GrievanceFull
import com.dmims.dmims.dataclass.StudentGrievanceGet

class GrievanceAdapterHodShow(userlist: ArrayList<StudentGrievanceGet>, context: Context) :
    RecyclerView.Adapter<GrievanceAdapterHodShow.ViewHolder>(), View.OnClickListener {
    private var ctx: Context? = null
    private var userlist: List<StudentGrievanceGet> = userlist


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        this.userlist = userlist
        this.ctx = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GrievanceAdapterHodShow.ViewHolder {

        val view = LayoutInflater.from(p0.context).inflate(R.layout.card_hodgriv_layout, p0, false)
        return ViewHolder(view, ctx!!, userlist)
    }


    override fun getItemCount(): Int {
        return userlist.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val emUser: StudentGrievanceGet = userlist[p1]
        p0.camera_image.setImageResource(emUser.image)
        p0.tv_Date?.text = emUser.G_DATE
        p0.tv_GrievanceId?.text = emUser.G_ID
        p0.tv_NameStud?.text = emUser.Grev_name
        p0.tv_subject?.text = emUser.G_SUBJECT
        p0.category?.text = emUser.G_CATEGORY
        p0.complaintAgainst?.text = emUser.G_AGAINST
        p0.grivComment?.text = emUser.G_COMMENT
        p0.tv_complaintTo?.text = emUser.Comp_To
        p0.tv_departmentTo?.text = emUser.DEPARTMENT



        if (emUser.G_STATUS.equals("Open")) {
            p0.grievStatus?.text = emUser.G_STATUS
            p0.grievStatus.setTextColor(Color.parseColor("#00FF00"))
        } else {
            p0.grievStatus?.text = emUser.G_STATUS
            p0.grievStatus.setTextColor(Color.parseColor("#FF0000"))
        }
        p0.grivDescription.text = emUser.G_DISCRIPTION


        p0.ll_More.visibility = View.GONE
        p0.moreContent.setOnClickListener {

            p0.ll_More.visibility = View.VISIBLE
            p0.moreContent.visibility = View.GONE
            println("more content")
            emUser.down_panal = true
        }

        p0.lessContent.setOnClickListener {

            p0.ll_More.visibility = View.GONE
            p0.moreContent.visibility = View.VISIBLE

            println("more content")
            emUser.down_panal = false
        }

        if (emUser.down_panal) {
            p0.ll_More.visibility = View.VISIBLE
            p0.moreContent.visibility = View.GONE
            println("more content")
        } else {
            p0.ll_More.visibility = View.GONE
            p0.moreContent.visibility = View.VISIBLE
            println("more content")
        }

        if (emUser.G_COMMENT.equals("")) {
            p0.ll_comment.visibility = View.GONE
        } else {
            p0.ll_comment.visibility = View.VISIBLE
        }


    }


    class ViewHolder(itemView: View, ctx: Context, list: List<StudentGrievanceGet>) :
        RecyclerView.ViewHolder(itemView) {


        val tv_Date = itemView.findViewById<TextView>(R.id.tv_date)
        val tv_GrievanceId = itemView.findViewById<TextView>(R.id.tv_GrievanceId)
        val tv_NameStud = itemView.findViewById<TextView>(R.id.tv_NameStud)
        val tv_subject = itemView.findViewById<TextView>(R.id.tv_subject)
        val category = itemView.findViewById<TextView>(R.id.tv_category)
        val complaintAgainst = itemView.findViewById<TextView>(R.id.tv_complaintAgainst)
        val grievStatus = itemView.findViewById<TextView>(R.id.tv_GrievanceStatus)
        val grivDescription = itemView.findViewById<TextView>(R.id.tv_description)
        val grivComment = itemView.findViewById<TextView>(R.id.tv_GrievanceComment)

        val tv_complaintTo = itemView.findViewById<TextView>(R.id.tv_complaintTo)
        val tv_departmentTo = itemView.findViewById<TextView>(R.id.tv_departmentTo)


        // val viewfile=itemView.findViewById<TextView>(R.id.tv_viewFile)

        val moreContent = itemView.findViewById<ImageView>(R.id.iv_moreContent)
        val lessContent = itemView.findViewById<ImageView>(R.id.iv_less_Content)
        val ll_More = itemView.findViewById<LinearLayout>(R.id.ll_moreContent)
        val ll_comment = itemView.findViewById<LinearLayout>(R.id.ll_comment)
        val ll_onClick = itemView.findViewById<LinearLayout>(R.id.ll_click)
        val ll_ViewAttachment = itemView.findViewById<LinearLayout>(R.id.view_attachment)

        val camera_image = itemView.findViewById<ImageView>(R.id.camera_image)

        init {

            ll_onClick.setOnClickListener {

                var s = list.get(adapterPosition)
                var b = s.G_ID
                println("b   " + b)
                var intent: Intent = Intent(ctx, GrievanceFull::class.java)
                intent.putExtra("id", b)
                ctx.startActivity(intent)


            }


            ll_ViewAttachment.setOnClickListener {


                var s = list.get(adapterPosition)
                var b = s.ATTACHMENT_URL

                if (s.G_ATTACHMENT.equals("F")) {
                    GenericUserFunction.DisplayToast(ctx, "No attachment available")
                } else if ((b.contains(".jpg", ignoreCase = true)) || (b.contains(
                        ".jpeg",
                        ignoreCase = true
                    )) || (b.contains(".png", ignoreCase = true))
                ) {
                    var intent: Intent = Intent(ctx, Common_Image_Viewer::class.java)
                    intent.putExtra("url", s.ATTACHMENT_URL)
                    intent.putExtra("actionTitle", "Image Viewer")
                    ctx.startActivity(intent)
                } else //Code for PDF
                    if (b.contains(".pdf", ignoreCase = true)) {
                        var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
                        intent.putExtra("url", s.ATTACHMENT_URL)
                        intent.putExtra("actionTitle", "PDF Viewer")
                        ctx.startActivity(intent)
                    }
            }
        }

    }


}