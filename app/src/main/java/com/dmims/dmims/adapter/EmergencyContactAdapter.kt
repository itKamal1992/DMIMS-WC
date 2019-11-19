package com.dmims.dmims.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.dataclass.EmUser
import com.dmims.dmims.R
import kotlinx.android.synthetic.main.list_layout.view.*
import java.lang.Exception

class EmergencyContactAdapter(context: Context,val userlist: ArrayList<EmUser>) :
    RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var emUser: List<EmUser> = userlist

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.list_layout, p0, false)
        return ViewHolder(v, ctx!!, emUser)
    }
    init {
        this.emUser = userlist
        this.ctx = context
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val cc = userlist[p1]
        p0.setData(cc, p1)
    }

    class ViewHolder(itemView: View, ctx: Context, contacts: List<EmUser>) : RecyclerView.ViewHolder(itemView) {


        var emUser: EmUser? = null
        var currentPosition: Int = 0

init {
    itemView.setOnClickListener {
try
        {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            var u:Uri?=null
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + emUser!!.contact)
            ctx!!.startActivity(intent)
        }
        catch (ex:Exception ) {
            GenericUserFunction.DisplayToast(ctx,ex.localizedMessage)
        }
    }
}


        fun setData(cc: EmUser?, position: Int) {
            itemView.txtpurpose?.text="Department : \n"+cc!!.purpose
            itemView.txtname?.text="Contact person : "+cc!!.name
            itemView.txtcontact?.text= Html.fromHtml("Contact No :<b> <font size=3 color=#4A89DC>"+cc!!.contact+"</font></b>")//("Contact No : <color:'#abc123'>"+cc!!.contact+"</color>")
//            itemView.txtcontact?.text="Contact No : "+cc!!.contact
            itemView.camera_image?.setImageResource(cc!!.image)
            this.emUser = cc
            this.currentPosition = position

        }

    }

}