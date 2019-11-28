package com.dmims.dmims.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.activity.Admin_CurrentNotification
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import kotlinx.android.synthetic.main.admin_adapter_notification.view.*
import kotlinx.android.synthetic.main.custom_layout.view.*
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.*
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.camera_image
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.txtNOTICE_DATE
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.txtNOTICE_TITLE
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.txtUSER_ROLE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.notice_adaptercurrent.view.txtStudYear as txtStudYear1

private lateinit var mServices: IMyAPI

class NoticeDeleteAdapterCurrent(userlist: ArrayList<NoticeStudCurrent>, context: Context) :
    RecyclerView.Adapter<NoticeDeleteAdapterCurrent.ViewHolder>(), View.OnClickListener {
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
        val v =
            LayoutInflater.from(p0.context).inflate(R.layout.admin_adapter_notification, p0, false)
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
//            itemView.setOnClickListener {
////                if(currenNotice!!.FILENAME.toString() == "FILENAME: -" || currenNotice!!.RESOU_FLAG.toString() == "ATTACHMENT STATUS: F")
////                {
//                    Toast.makeText(itemView.context, "NO Attachment for this notice"+currenNotice!!.DEPT_ID, Toast.LENGTH_SHORT).show()
////                }
////                else
////                {
////                var intent: Intent = Intent(ctx, StudNoticeData::class.java)
////                intent.putExtra("urlimg", currenNotice!!.FILENAME)
////                ctx.startActivity(intent)
////
////                }
//                //
//            }
            itemView.setOnLongClickListener {
                //                Toast.makeText(itemView.context, "NO Attachment for this notice"+currenNotice!!.DEPT_ID, Toast.LENGTH_SHORT).show()
//                DeleteNotice
                println("userlist >>> " + contacts)


                //////////Start///////////////
                GenericPublicVariable.CustDialog = Dialog(ctx)
                GenericPublicVariable.CustDialog.setContentView(R.layout.dialog_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button =
                    GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCancel: Button =
                    GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView =
                    GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                tvMsg.text = "Do you really want to delete this Notice?"
                GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()
                    delete(ctx)

                }
                btnCancel.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()

                }
                ivNegClose1.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()
                }
                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                GenericPublicVariable.CustDialog.show()
                //////////End//////////////


                return@setOnLongClickListener true
            }

        }

        fun setData(cc: NoticeStudCurrent?, position: Int) {
            itemView.txtNOTICE_TITLE?.text = "Title :" + cc!!.NOTICE_TITLE
            itemView.txtNOTICE_DATE?.text = "Date :" + cc.NOTICE_DATE
            itemView.txtUSER_ROLE?.text = "From :" + cc.USER_ROLE
            itemView.txtDept?.text = "Department belongs : " + cc.DEPT_NAME

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

            itemView.txtPara?.text =
                "Dear $after,\nYou have received notice from " + cc.USER_ROLE + ", stating as \"" + cc.NOTICE_DESC + "\".\nHence we request you to please address the notice as soon as possible."

            itemView.txtRegards?.text = "Regards,\nDMIMS APP"
//            itemView.txtNOTICE_DESC?.text = cc!!.NOTICE_DESC
//
//            itemView.txtINSTITUTE_NAME?.text = cc!!.INSTITUTE_NAME
//            itemView.txtCOURSE_NAME?.text = cc!!.COURSE_NAME
//            itemView.txtCOURSE_ID?.text = cc!!.COURSE_ID
//            itemView.txtDEPT_NAME?.text = cc!!.NOTICE_TYPE
//            itemView.txtDEPT_ID?.text = cc!!.DEPT_ID
//            itemView.txtDEPT_NAME?.text = cc!!.DEPT_NAME
////            itemView.txtRESOU_FLAG?.text = cc!!.RESOU_FLAG
////            itemView.txtFILENAME?.text = cc!!.FILENAME
            itemView.camera_image?.setImageResource(cc!!.image)
            this.currenNotice = cc
            this.currentPosition = position
        }

        fun delete(ctx: Context) {
            if (InternetConnection.checkConnection(ctx)) {
                try {
                    mServices = Common.getAPI()

                    mServices.DeleteNotice((currenNotice!!.DEPT_ID).toInt())
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                                Toast.makeText(itemView.context, t.message, Toast.LENGTH_SHORT)
                                    .show()
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE

                            }

                            override fun onResponse(
                                call: Call<APIResponse>,
                                response: Response<APIResponse>
                            ) {
                                val result: APIResponse? = response.body()
                                println("result 1 >>> " + result.toString())
                                if (result!!.Responsecode == 200) {
                                    Toast.makeText(
                                        itemView.context,
                                        result.Status,
                                        Toast.LENGTH_SHORT
                                    ).show()
//                            GenericUserFunction.showPositivePopUp(ctx,"Notice deleted successfully.")
                                    val intent = Intent(ctx, ctx::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    ctx.startActivity(intent)

                                } else {

                                    Toast.makeText(
                                        itemView.context,
                                        result.Status,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        ctx,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            } else {
                GenericUserFunction.showInternetNegativePopUp(
                    ctx, ctx.getString(R.string.failureNoInternetErr)
                )
            }
        }


    }

}