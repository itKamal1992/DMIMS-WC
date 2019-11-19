package com.dmims.dmims.adapter

import android.content.Context
import android.database.DataSetObserver
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.MultiSelectData
import kotlinx.android.synthetic.main.multi_select_adapter.view.*
import kotlin.collections.ArrayList

class MultiSelectAdapter_U(userlist: ArrayList<MultiSelectData>, context: Context) :
    RecyclerView.Adapter<MultiSelectAdapter_U.ViewHolder>(), View.OnClickListener, SpinnerAdapter {
    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewTypeCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDropDownView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return userlist.size
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var ctx: Context? = null
    private var userlist: List<MultiSelectData> = userlist
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        this.userlist = userlist
        this.ctx = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.multi_select_adapter, p0, false)
        return ViewHolder(v, ctx!!, userlist)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val cc = userlist[p1]
        p0.setData(cc, p1)
    }

    class ViewHolder(itemView: View, ctx: Context, contacts: List<MultiSelectData>) :
        RecyclerView.ViewHolder(itemView) {
        var currenLayout: MultiSelectData? = null
        var currentPosition: Int = 0

        init {
//            itemView.setOnClickListener {
//                    if (currenLayout!!.FILENAME.toString() == "FILENAME: -" || currenLayout!!.RESOU_FLAG.toString() == "ATTACHMENT STATUS: F") {
//                        Toast.makeText(itemView.context, "NO Attachment for this notice", Toast.LENGTH_SHORT).show()
//                    } else
//               {
//                // Code for JGP, PNG
//                if ((currenLayout!!.FILENAME.contains(".jpg",ignoreCase = true))||(currenLayout!!.FILENAME.contains(".png",ignoreCase = true)))
//                {
//                    var intent: Intent = Intent(ctx, Common_Image_Viewer::class.java)
//                    intent.putExtra("url", currenLayout!!.FILENAME)
//                    intent.putExtra("actionTitle", "Image Viewer")
//                    ctx.startActivity(intent)
//                }
//                else //Code for PDF
//                    if (currenLayout!!.FILENAME.contains(".pdf",ignoreCase = true))
//                    {
//                        var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
//                        intent.putExtra("url", currenLayout!!.FILENAME)
//                        intent.putExtra("actionTitle", "PDF Viewer")
//                        ctx.startActivity(intent)
//                    }
//
//
//
//            }
////                  else  {
////                    var intent: Intent = Intent(ctx, StudNoticeData::class.java)
////                    intent.putExtra(
////                        "urlimg", currenLayout!!.FILENAME
////                    )
////                    ctx.startActivity(intent)
////                        val dialog: AlertDialog = SpotsDialog.Builder().setContext(ctx).build()
////                        dialog.show()
////                        dialog.setMessage("Downloading ...")
////
////
////                        var image_Url: String = currenLayout!!.FILENAME
////                        var ext: String? = null
////                        if (image_Url.contains(".jpg")) {
////                            ext = ".jpg"
////                        }
////                        if (image_Url.contains(".png")) {
////                            ext = ".png"
////                        }
////
////
////                        var fileName = UUID.randomUUID().toString() + ext
////                        Picasso.with(ctx)
////                            .load(image_Url)
////                            .into(
////                                SaveImageHelper(
////                                    ctx,
////                                    dialog,
////                                    ctx.contentResolver,
////                                    fileName,
////                                    "Image Description"
////                                )
////                            )
//
//
////                    }
//                }
                //

            
        }

        fun setData(cc: MultiSelectData?, position: Int) {
            itemView.txt_year?.text = cc!!.CheckBox_Title
            itemView.chk_year?.isChecked=cc!!.CheckBox_Status
//            itemView.txtUSER_TYPE?.text = cc.USER_TYPE
//            itemView.txtNOTICE_TYPE?.text = cc.NOTICE_TYPE
//            itemView.txtNOTICE_DESC?.text = cc.NOTICE_DESC
//            itemView.txtNOTICE_DATE?.text = cc.NOTICE_DATE
//            itemView.txtINSTITUTE_NAME?.text = cc.INSTITUTE_NAME
//            itemView.txtCOURSE_NAME?.text = cc.COURSE_NAME
//            itemView.txtCOURSE_ID?.text = cc.COURSE_ID
//            itemView.txtDEPT_NAME?.text = cc.NOTICE_TYPE
//            itemView.txtDEPT_ID?.text = cc.DEPT_ID
//            itemView.txtDEPT_NAME?.text = cc.DEPT_NAME
////            itemView.txtRESOU_FLAG?.text = cc!!.RESOU_FLAG
////            itemView.txtFILENAME?.text = cc!!.FILENAME
//            itemView.camera_image?.setImageResource(cc.image)
//            this.currenLayout = cc
//            this.currentPosition = position
        }

    }

}