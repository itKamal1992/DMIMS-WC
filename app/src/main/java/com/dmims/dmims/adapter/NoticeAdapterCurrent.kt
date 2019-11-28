package com.dmims.dmims.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.common.SaveImageHelper
import com.dmims.dmims.common.SaveImageHelpers
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.*
import java.util.*
import kotlin.collections.ArrayList

class NoticeAdapterCurrent(userlist: ArrayList<NoticeStudCurrent>, context: Context) :
    RecyclerView.Adapter<NoticeAdapterCurrent.ViewHolder>(), View.OnClickListener {
    private var ctx: Context? = null
    private var userlist: List<NoticeStudCurrent> = userlist
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        this.userlist = userlist
        this.ctx = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.notice_adaptercurrent, p0, false)
        return ViewHolder(v, ctx!!, userlist)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val cc = userlist[p1]
        p0.setData(cc, p1)
    }

    class ViewHolder(itemView: View, ctx: Context, contacts: List<NoticeStudCurrent>) :
        RecyclerView.ViewHolder(itemView) {
        var currenNotice: NoticeStudCurrent? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                    if (currenNotice!!.FILENAME.toString() == "FILENAME: -" || currenNotice!!.RESOU_FLAG.toString() == "ATTACHMENT STATUS: F") {
                        Toast.makeText(itemView.context, "NO Attachment for this notice", Toast.LENGTH_SHORT).show()
                    } else
               {
                // Code for JGP, PNG
                if ((currenNotice!!.FILENAME.contains(".jpg",ignoreCase = true))||(currenNotice!!.FILENAME.contains(".jpeg",ignoreCase = true))||(currenNotice!!.FILENAME.contains(".png",ignoreCase = true)))
                {
                    var intent: Intent = Intent(ctx, Common_Image_Viewer::class.java)
                    intent.putExtra("url", currenNotice!!.FILENAME)
                    intent.putExtra("actionTitle", "Image Viewer")
                    ctx.startActivity(intent)
                }
                else //Code for PDF
                    if (currenNotice!!.FILENAME.contains(".pdf",ignoreCase = true))
                    {
                        var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
                        intent.putExtra("url", currenNotice!!.FILENAME)
                        intent.putExtra("actionTitle", "PDF Viewer")
                        ctx.startActivity(intent)
                    }



            }
//                  else  {
//                    var intent: Intent = Intent(ctx, StudNoticeData::class.java)
//                    intent.putExtra(
//                        "urlimg", currenNotice!!.FILENAME
//                    )
//                    ctx.startActivity(intent)
//                        val dialog: AlertDialog = SpotsDialog.Builder().setContext(ctx).build()
//                        dialog.show()
//                        dialog.setMessage("Downloading ...")
//
//
//                        var image_Url: String = currenNotice!!.FILENAME
//                        var ext: String? = null
//                        if (image_Url.contains(".jpg")) {
//                            ext = ".jpg"
//                        }
//                        if (image_Url.contains(".png")) {
//                            ext = ".png"
//                        }
//
//
//                        var fileName = UUID.randomUUID().toString() + ext
//                        Picasso.with(ctx)
//                            .load(image_Url)
//                            .into(
//                                SaveImageHelper(
//                                    ctx,
//                                    dialog,
//                                    ctx.contentResolver,
//                                    fileName,
//                                    "Image Description"
//                                )
//                            )


//                    }
                }
                //
        }

        fun setData(cc: NoticeStudCurrent?, position: Int) {
            itemView.txtNOTICE_TITLE?.text = cc!!.NOTICE_TITLE
            itemView.txtUSER_ROLE?.text = cc.USER_ROLE
            itemView.txtUSER_TYPE?.text = cc.USER_TYPE
            itemView.txtNOTICE_TYPE?.text = cc.NOTICE_TYPE
            itemView.txtNOTICE_DESC?.text = cc.NOTICE_DESC

            var after=""
            if (cc.STUDENT_FLAG == "T") {
                itemView.txtStudYear?.text = "Student in Year : " + cc.YEAR
                after="student in year "+cc.YEAR
            }
            if (cc.FACULTY_FLAG == "T") {
                itemView.txtStudYear?.text = "Faculty in Year : " + cc.YEAR
                after="faculty in year "+cc.YEAR
            }

            if (cc.STUDENT_FLAG == "T" && cc.FACULTY_FLAG == "T") {
                itemView.txtStudYear?.text = "Faculty and Students in Year : " + cc.YEAR
                after="faculty and students in year "+cc.YEAR
            }

            itemView.txtNOTICE_DATE?.text = cc.NOTICE_DATE
            itemView.txtINSTITUTE_NAME?.text = cc.INSTITUTE_NAME
            itemView.txtCOURSE_NAME?.text = cc.COURSE_NAME
//            itemView.txtDEPT_ID?.text = cc.DEPT_ID
            itemView.txtDEPT_NAME?.text = cc.DEPT_NAME
//            itemView.txtRESOU_FLAG?.text = cc!!.RESOU_FLAG
//            itemView.txtFILENAME?.text = cc!!.FILENAME
            itemView.camera_image?.setImageResource(cc.image)
            this.currenNotice = cc
            this.currentPosition = position
        }

    }

}