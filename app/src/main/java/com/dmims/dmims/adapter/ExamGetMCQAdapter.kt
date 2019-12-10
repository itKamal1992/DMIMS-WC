/* Created by Umesh Gaidhane*/
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
import com.dmims.dmims.activity.Common_Image_Viewer
import com.dmims.dmims.activity.Common_PDF_Viewer
import com.dmims.dmims.activity.StudNoticeData
import com.dmims.dmims.dataclass.GreivanceCellData
import com.dmims.dmims.dataclass.McqFields
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExamGetMCQAdapter(val userlist: ArrayList<McqFields>,context: Context) :
    RecyclerView.Adapter<ExamGetMCQAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var mcqList: List<McqFields> = userlist

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.exam_get_mcq_adapter_layout, p0, false)
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
                            intent.putExtra("actionTitle", "Exam Key Viewer")
                            ctx.startActivity(intent)
                        }



                }

            }

            itemView.setOnLongClickListener {
                //////////Start///////////////
                GenericPublicVariable.CustDialog = Dialog(ctx)
                GenericPublicVariable.CustDialog.setContentView(R.layout.dialog_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCancel: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                tvMsg.text = "Do you really want to delete this Exam Key?"
                GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()
                    delete(ctx,mcqFields!!)

                }
                btnCancel.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()

                }
                ivNegClose1.setOnClickListener {
                    GenericPublicVariable.CustDialog.dismiss()
                }
                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT)
                )
                GenericPublicVariable.CustDialog.show()
                //////////End//////////////


                return@setOnLongClickListener true
            }
        }

        fun setData(cc: McqFields?, position: Int) {
            itemView.txtNOTICE_TITLE?.text = "MCQ Answer Key "+cc!!.Institute
            itemView.txtNOTICE_DATE?.text = "Date : "+cc!!.StartDate
            itemView.txtUSER_ROLE?.text = cc!!.UserRole
            itemView.txtPara?.text = "Dear student,\nYou have received MCQ Answer Key from "+cc!!.UserDesig+ " "+cc!!.UserName+" available for you from "+cc!!.StartDate+" till " +cc!!.EndDate+"."+"\nHence we request you to please address the Answer Key as soon as possible."
            itemView.txtRegards?.text ="Regards, \nDMIMS APP"
            itemView.camera_image?.setImageResource(cc.image)
            this.mcqFields = cc
            this.currentPosition = position
        }

        fun delete(ctx: Context,mcqFields:McqFields)
        {
            try {
                if (mcqFields!!.id.isEmpty()) {
                    Toast.makeText(
                        ctx,
                        "NO Attachment for this Exam Key",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                {
                    if (InternetConnection.checkConnection(ctx)) {
                        val dialog: android.app.AlertDialog =
                            SpotsDialog.Builder().setContext(ctx).build()
                        dialog.setMessage("Please Wait!!! \nwhile we delete ExamKey")
                        dialog.setCancelable(false)
                        dialog.show()

                    var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                        PhpApiInterface::class.java
                    )
                    var call3: Call<APIResponse> =
                        phpApiInterface.deleteMCQ(mcqFields!!.id)
                    call3.enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            dialog.dismiss()
                            Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            dialog.dismiss()
                            val result: APIResponse? =
                                response.body()
                            result!!.response
                            if (result!!.response!!.contains("successfully"))
                            {

                                println("result!!.response >>> "+result!!.response)
//                                GenericUserFunction.showPositivePopUp(ctx,result!!.response!!)
                                val intent = Intent(ctx, ctx::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                ctx.startActivity(intent)


                            }else
                            {
//                                GenericUserFunction.showOopsError(ctx,result!!.response!!)
                                val intent = Intent(ctx, ctx::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                ctx.startActivity(intent)
                            }

                        }
                    })
                }
                else
                {
                    GenericUserFunction.showInternetNegativePopUp(
                        ctx,
                        ctx.getString(R.string.failureNoInternetErr))
                }

                }
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    ctx,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }
    }

}