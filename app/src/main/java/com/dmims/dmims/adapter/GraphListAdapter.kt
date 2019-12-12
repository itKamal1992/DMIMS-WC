/* Created by Umesh Gaidhane*/
package com.dmims.dmims.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.dmims.dmims.dataclass.GraphData

import com.dmims.dmims.dataclass.GreivanceCellData
import com.dmims.dmims.dataclass.NoticeStudCurrent
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.*
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.camera_image
import kotlinx.android.synthetic.main.exam_get_mcq_adapter_layout.view.txtNOTICE_TITLE
import kotlinx.android.synthetic.main.exam_inbox_get_mcq_adapter_layout.view.*
import kotlinx.android.synthetic.main.graph_item_adapter_layout.view.*

class GraphListAdapter(val userlist: ArrayList<GraphData>, context: Context) :
    RecyclerView.Adapter<GraphListAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var mcqList: List<GraphData> = userlist

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.graph_item_adapter_layout, p0, false)
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

    class ViewHolder(itemView: View, ctx: Context, contacts: List<GraphData>) : RecyclerView.ViewHolder(itemView) {

        var GraphData: GraphData? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                println("FileUrl >> "+GraphData!!.graph_desc)
//                                if (!GraphData!!.FileUrl.contains("http")) {
//                    Toast.makeText(itemView.context, "NO Attachment for this notice", Toast.LENGTH_SHORT).show()
//                }
//                else {
//                    // Code for JGP, PNG
//                    if ((GraphData!!.FileUrl.contains(".jpg",ignoreCase = true))||(GraphData!!.FileUrl.contains(".jpeg",ignoreCase = true))||(GraphData!!.FileUrl.contains(".png",ignoreCase = true)))
//                    {
//                        var intent: Intent = Intent(ctx, Common_Image_Viewer::class.java)
//                        intent.putExtra(
//                            "url", GraphData!!.FileUrl
//                        )
//                        ctx.startActivity(intent)
//                    }
//                    else //Code for PDF
//                        if (GraphData!!.FileUrl.contains(".pdf",ignoreCase = true))
//                        {
//                            var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
//                            intent.putExtra("url", GraphData!!.FileUrl)
//                            intent.putExtra("actionTitle", "PDF Viewer")
//                            ctx.startActivity(intent)
//                        }
//
//
//
//                }

            }

           itemView.setOnLongClickListener {

               return@setOnLongClickListener true
           }
        }

        fun setData(cc: GraphData?, position: Int) {
//            itemView.txtNOTICE_TITLE?.text = "Title : Answer Key"
//            itemView.txtNOTICE_DATE?.text = "Date : "+cc!!.StartDate
//            itemView.txtUSER_ROLE?.text = cc!!.UserRole
//            itemView.txtPara?.text = "Dear student,\nYou have received MCQ Answer Key from "+cc!!.UserDesig+ " "+cc!!.UserName+" available for you from "+cc!!.StartDate+" till" +cc!!.EndDate+"."+"\nHence we request you to please address the Answer Key as soon as possible."
//            itemView.txtRegards?.text ="Regards, \nDMIMS APP"
//            itemView.linear_field_layout.setBackgroundColor(Color.parseColor(cc!!.colorcode))
            itemView.card_view_innders.setCardBackgroundColor(Color.parseColor("#abc121"))
            itemView.txt_faculty ?.text="Faculty : "+cc!!.graph_desc
            itemView.txt_count ?.text="Count :"+cc!!.No_of_bar
            itemView.txt_percentage ?.text="Percent :"//+cc.percentage
            itemView.barchart5

            this.currentPosition = position
        }
    }

}