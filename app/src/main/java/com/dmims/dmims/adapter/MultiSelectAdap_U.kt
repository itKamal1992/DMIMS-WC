package com.dmims.dmims.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.dmims.dmims.dataclass.MultiSelectData
import android.view.LayoutInflater
import android.R
import android.graphics.Color
import android.widget.*
import android.graphics.Color.parseColor
import android.opengl.Visibility
import android.widget.TextView




class MultiSelectAdap_U: BaseAdapter ,SpinnerAdapter, AdapterView.OnItemSelectedListener {



    var colors = arrayOf("#13edea", "#e20ecd", "#15ea0d","#13edea", "#e20ecd", "#15ea0d")
    var colorsback = arrayOf("#FF000000", "#FFF5F1EC", "#ea950d")
    var context: Context? = null
//    var multiSelectData: MultiSelectData? = null
    private var multiSelectData: List<MultiSelectData>
    var inflter: LayoutInflater? = null
    constructor(context: Context?, multiSelectData: ArrayList<MultiSelectData>) : super() {
        this.context = context
        this.multiSelectData = multiSelectData
        inflter = (LayoutInflater.from(context))
    }


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = inflter!!.inflate(com.dmims.dmims.R.layout.multi_select_adapter, p2, false)
        var year_txt:TextView=view.findViewById(com.dmims.dmims.R.id.txt_year)
        var chk_year:CheckBox=view.findViewById(com.dmims.dmims.R.id.chk_year)
        year_txt.text=multiSelectData[p0].CheckBox_Title
        if (p0==0){
            chk_year.visibility=View.GONE
        }
        else {
            chk_year.isChecked = multiSelectData[p0].CheckBox_Status
        }
        return view
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Toast.makeText(context,"selected >> "+multiSelectData[p2] , Toast.LENGTH_LONG).show()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        return super.getDropDownView(position, convertView, parent)
//        val view: View
        val view = inflter!!.inflate(com.dmims.dmims.R.layout.multi_select_adapter, parent, false)
//        view = View.inflate(context, com.dmims.dmims.R.layout.multi_select_adapter, null)
        var year_txt:TextView=view.findViewById(com.dmims.dmims.R.id.txt_year)
        var chk_year:CheckBox=view.findViewById(com.dmims.dmims.R.id.chk_year)
        year_txt.text=multiSelectData[position].CheckBox_Title
        chk_year.isChecked=multiSelectData[position].CheckBox_Status

//        year_txt.setTextColor(Color.parseColor(colors[position]))
//        textView.setBackgroundColor(Color.parseColor(colorsback[position]))
        if (position==0){
            chk_year.visibility=View.GONE
        }
        else {
            chk_year.isChecked = multiSelectData[position].CheckBox_Status
        }

        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return multiSelectData.size
    }


}