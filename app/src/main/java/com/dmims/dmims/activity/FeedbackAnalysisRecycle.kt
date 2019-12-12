package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mServices
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.GraphListAdapter
import com.dmims.dmims.dataclass.BarDatas
import com.dmims.dmims.dataclass.GraphData
import com.dmims.dmims.dataclass.GraphFields
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.GetGraphList
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_feedback_analysis1.*
import kotlinx.android.synthetic.main.activity_feedback_analysis_recycle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Float.parseFloat
import java.util.*

class FeedbackAnalysisRecycle : AppCompatActivity() {


    private var users: ArrayList<GraphFields>? = null
    lateinit var mpBarChart:  BarChart
    lateinit var mpBarChart1: BarChart
    lateinit var mpBarChart2: BarChart
    lateinit var mpBarChart3: BarChart
    lateinit var mpBarChart4: BarChart
    lateinit var mpBarChart5: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_analysis_recycle)
        val recyclerView = findViewById<RecyclerView>(R.id.GraphType)
        GraphType.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)



//        mpBarChart1 = findViewById(R.id.barchart1)
//        mpBarChart2 = findViewById(R.id.barchart2)
//        mpBarChart3 = findViewById(R.id.barchart3)
//        mpBarChart4 = findViewById(R.id.barchart4)
//        mpBarChart5 = findViewById(R.id.barchart5)
//        btn_webGraph.setOnClickListener {
//            this.startActivity( Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("http://dmimsdu.in/web/test_folder/graph_report.php")
//            ))
//        }





        mserviceGraphData()
    }

//    fun bardraw(
//        barDataSet1: BarDataSet,
//        barDataSet2: BarDataSet,
//        barDataSet3: BarDataSet,
//        barDataSet4: BarDataSet,
//        barDataSet5: BarDataSet,
//        barDataSet6: BarDataSet
//    ) {
//
//
//        val data =
//            BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4, barDataSet5, barDataSet6)
//        mpBarChart.data = data
//        mpBarChart.isHighlightFullBarEnabled
//        val xAxis = mpBarChart.xAxis
//        xAxis.textSize = 10f
//        xAxis.setCenterAxisLabels(true)
//        xAxis.position = XAxis.XAxisPosition.TOP
//        xAxis.granularity = 1f
//        xAxis.isGranularityEnabled = true
//        mpBarChart.isDragEnabled = true
//        mpBarChart.setVisibleXRangeMaximum(2f)
//        val barSpace = 0.01f
//        val groupSpace = 0.00f
//        data.barWidth = 0.16f
//        mpBarChart.xAxis.axisMinimum = 0f
//        mpBarChart.xAxis.axisMaximum = 1f
//        mpBarChart.axisLeft.axisMinimum = 0f
//        mpBarChart.groupBars(0.1f, 0.0f, 0.05f)
//        mpBarChart.invalidate()
//    }

//    private fun barEntries1(): ArrayList<BarEntry> {
//        val barEntries1 = ArrayList<BarEntry>()
//        barEntries1.add(BarEntry(1f, 200f))
//        return barEntries1
//    }
//
//    private fun barEntries2(): ArrayList<BarEntry> {
//        val barEntries2 = ArrayList<BarEntry>()
//        barEntries2.add(BarEntry(2f, 40f))
//        return barEntries2
//    }
//
//    private fun barEntries3(): ArrayList<BarEntry> {
//        val barEntries3 = ArrayList<BarEntry>()
//        barEntries3.add(BarEntry(2f, 40f))
//        return barEntries3
//    }
//
//    private fun barEntries4(): ArrayList<BarEntry> {
//        val barEntries4 = ArrayList<BarEntry>()
//        barEntries4.add(BarEntry(2f, 40f))
//        return barEntries4
//    }
//
//    private fun barEntries5(): ArrayList<BarEntry> {
//        val barEntries5 = ArrayList<BarEntry>()
//        barEntries5.add(BarEntry(2f, 40f))
//        return barEntries5
//    }
//
//    private fun barEntries6(): ArrayList<BarEntry> {
//        val barEntries6 = ArrayList<BarEntry>()
//        barEntries6.add(BarEntry(2f, 40f))
//        return barEntries6
//    }
//    val barEntries3 = ArrayList<BarEntry>()
//    barEntries3.add(BarEntry(3f, 0f))
//
//
//    val barEntries4 = ArrayList<BarEntry>()
//    barEntries4.add(BarEntry(4f, 0f))
//
//
//    val barEntries5 = ArrayList<BarEntry>()
//    barEntries5.add(BarEntry(5f, 0f))
//
//    val barEntries6 = ArrayList<BarEntry>()
//    barEntries6.add(BarEntry(6f, 0f))

    fun mserviceGraphData() {
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are updating Data for Graph")
            dialog.setCancelable(false)
            dialog.show()
            try {
                mServices.GetGraph("2019")
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@FeedbackAnalysisRecycle, t.message, Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 200) {
                                val users = ArrayList<GraphData>()
                                val BarDataList01 = ArrayList<BarDatas>()
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Medicine_A1.count,result.Data20!!.FBUG.Medicine_A1.percent,result.Data20!!.FBUG.Medicine_A1.color,"Medicine_A1"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Dental_A2.count,result.Data20!!.FBUG.Dental_A2.percent,result.Data20!!.FBUG.Dental_A2.color,"Dental_A2"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Ayurveda_A3.count,result.Data20!!.FBUG.Ayurveda_A3.percent,result.Data20!!.FBUG.Ayurveda_A3.color,"Ayurveda_A3"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Nursing_A4.count,result.Data20!!.FBUG.Nursing_A4.percent,result.Data20!!.FBUG.Nursing_A4.color,"Nursing_A4"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Inter_Disciplinary_A5.count,result.Data20!!.FBUG.Inter_Disciplinary_A5.percent,result.Data20!!.FBUG.Inter_Disciplinary_A5.color,"Inter_Disciplinary_A5"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.No_Answer.count,result.Data20!!.FBUG.No_Answer.percent,result.Data20!!.FBUG.No_Answer.color,"No_Answer"))
                                BarDataList01.add(BarDatas(result.Data20!!.FBUG.Physiotherapy.count,result.Data20!!.FBUG.Physiotherapy.percent,result.Data20!!.FBUG.Physiotherapy.color,"Physiotherapy"))
                                users.add(GraphData(result.Data20!!.FBUG.DESC,7,BarDataList01))

                                val BarDataList02 = ArrayList<BarDatas>()
                                BarDataList02.add(BarDatas(result.Data20!!.FBUG1.Summer_A1.count,result.Data20!!.FBUG1.Summer_A1.percent,result.Data20!!.FBUG1.Summer_A1.color,"Summer_A1"))
                                BarDataList02.add(BarDatas(result.Data20!!.FBUG1.Winter_A2.count,result.Data20!!.FBUG1.Winter_A2.percent,result.Data20!!.FBUG1.Winter_A2.color,"Winter_A2"))
                                BarDataList02.add(BarDatas(result.Data20!!.FBUG1.Comments.count,result.Data20!!.FBUG1.Comments.percent,result.Data20!!.FBUG1.Comments.color,"Comments"))
                                BarDataList02.add(BarDatas(result.Data20!!.FBUG1.No_Answer.count,result.Data20!!.FBUG1.No_Answer.percent,result.Data20!!.FBUG1.No_Answer.color,"No_Answer"))
                                users.add(GraphData(result.Data20!!.FBUG1.DESC,4,BarDataList02))

//                                users.add(GraphData(result.Data20!!.FBUG4_SQ001.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG4_SQ002.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG4_SQ003.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG4_SQ004.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG5_SQ001.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG6.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG8.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG9.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG14.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG15.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG16.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG17.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG18.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG19.DESC,4))
//                                users.add(GraphData(result.Data20!!.FBUG20.DESC,2))

                                if(users.isEmpty()){
                                    GenericUserFunction.showOopsError(
                                        this@FeedbackAnalysisRecycle,
                                        "No Graph found data for the current request"
                                    )
                                }else {

                                    val adapter = GraphListAdapter(users,this@FeedbackAnalysisRecycle)
                                    GraphType.adapter = adapter
                                }





//
//
//                                var faculty: String = result.Data20!!.FBUG.Medicine_A1.faculty
//                                var barcount1: Float =
//                                    parseFloat(result.Data20!!.FBUG.Medicine_A1.count)
//                                var color: String = result.Data20!!.FBUG.Medicine_A1.color
//                                val barEntries1 = ArrayList<BarEntry>()
//                                barEntries1.add(BarEntry(1f, barcount1))
//
//                                val barDataSet0 = BarDataSet(barEntries1, faculty)
//                                barDataSet0.color = Color.parseColor(color)
//                                barDataSet0.valueTextSize = 15f
//
//                                var facultyc: String = result.Data20!!.FBUG.Dental_A2.faculty
//                                var barcount1c: Float =
//                                    parseFloat(result.Data20!!.FBUG.Dental_A2.count)
//                                var colorc: String = result.Data20!!.FBUG.Dental_A2.color
//                                val barEntries1c = ArrayList<BarEntry>()
//                                barEntries1c.add(BarEntry(2f, barcount1c))
//                                val barDataSet1 = BarDataSet(barEntries1c, facultyc)
//                                barDataSet1.color = Color.parseColor(colorc)
//                                barDataSet1.valueTextSize = 15f
//
//
//                                var faculty2: String = result.Data20!!.FBUG.Ayurveda_A3.faculty
//                                var barcount12: Float =
//                                    parseFloat(result.Data20!!.FBUG.Ayurveda_A3.count)
//                                var color2: String = result.Data20!!.FBUG.Ayurveda_A3.color
//                                val barEntries12 = ArrayList<BarEntry>()
//                                barEntries12.add(BarEntry(2f, barcount12))
//                                val barDataSet2 = BarDataSet(barEntries12, faculty2)
//                                barDataSet2.color = Color.parseColor(color2)
//                                barDataSet2.valueTextSize = 15f
//
//                                var faculty3: String = result.Data20!!.FBUG.Nursing_A4.faculty
//                                var barcount3: Float =
//                                    parseFloat(result.Data20!!.FBUG.Nursing_A4.count)
//                                var color3: String = result.Data20!!.FBUG.Nursing_A4.color
//                                val barEntries3 = ArrayList<BarEntry>()
//                                barEntries3.add(BarEntry(2f, barcount3))
//
//                                val barDataSet3 = BarDataSet(barEntries3, faculty3)
//                                barDataSet3.color = Color.parseColor(color3)
//                                barDataSet3.valueTextSize = 15f
//
//                                var faculty4: String =
//                                    result.Data20!!.FBUG.Inter_Disciplinary_A5.faculty
//                                var barcount14: Float =
//                                    parseFloat(result.Data20!!.FBUG.Inter_Disciplinary_A5.count)
//                                var color4: String =
//                                    result.Data20!!.FBUG.Inter_Disciplinary_A5.color
//                                val barEntries14 = ArrayList<BarEntry>()
//                                barEntries14.add(BarEntry(2f, barcount14))
//
//                                val barDataSet4 = BarDataSet(barEntries14, faculty4)
//                                barDataSet4.color = Color.parseColor(color4)
//                                barDataSet4.valueTextSize = 15f
//
//                                var faculty5: String = result.Data20!!.FBUG.No_Answer.faculty
//                                var barcount15: Float =
//                                    parseFloat(result.Data20!!.FBUG.No_Answer.count)
//                                var color5: String = result.Data20!!.FBUG.No_Answer.color
//                                val barEntries15 = ArrayList<BarEntry>()
//                                barEntries15.add(BarEntry(2f, barcount15))
//
//                                val barDataSet5 = BarDataSet(barEntries15, faculty5)
//                                barDataSet5.color = Color.parseColor(color5)
//                                barDataSet5.valueTextSize = 15f
//
//
//                                var faculty6: String = result.Data20!!.FBUG.Physiotherapy.faculty
//                                var barcount16: Float =
//                                    parseFloat(result.Data20!!.FBUG.Physiotherapy.count)
//                                var color6: String = result.Data20!!.FBUG.Physiotherapy.color
//                                val barEntries16 = ArrayList<BarEntry>()
//                                barEntries16.add(BarEntry(2f, barcount16))
//
//                                val barDataSet6 = BarDataSet(barEntries16, faculty6)
//                                barDataSet6.color = Color.parseColor(color6)
//                                barDataSet6.valueTextSize = 15f
//
//
//                                val data =
//                                    BarData(
//                                        barDataSet0,
//                                        barDataSet1,
//                                        barDataSet2,
//                                        barDataSet3,
//                                        barDataSet4,
//                                        barDataSet5,
//                                        barDataSet6
//                                    )
//
//                                /*----------------------------------------------------------------*/
//
//                                var faculty02_01: String = result.Data20!!.FBUG1.Summer_A1.faculty
//                                var barcount1c02_01: Float =parseFloat(result.Data20!!.FBUG1.Summer_A1.count)
//                                var colorc02_01: String = result.Data20!!.FBUG1.Summer_A1.color
//                                val barEntries1c02_01 = ArrayList<BarEntry>()
//                                barEntries1c02_01.add(BarEntry(2f, barcount1c02_01))
//                                val barDataSet102_01 = BarDataSet(barEntries1c02_01, faculty02_01)
//                                barDataSet102_01.color = Color.parseColor(colorc02_01)
//                                barDataSet102_01.valueTextSize = 15f
//
//
//                                var faculty02_02: String = result.Data20!!.FBUG1.Winter_A2.faculty
//                                var barcount1c02_02: Float =
//                                    parseFloat(result.Data20!!.FBUG1.Winter_A2.count)
//                                var colorc02_02: String = result.Data20!!.FBUG1.Winter_A2.color
//                                val barEntries1c02_02 = ArrayList<BarEntry>()
//                                barEntries1c02_02.add(BarEntry(2f, barcount1c02_02))
//                                val barDataSet102_02 = BarDataSet(barEntries1c02_02, faculty02_02)
//                                barDataSet102_02.color = Color.parseColor(colorc02_02)
//                                barDataSet102_02.valueTextSize = 15f
//
//                                var faculty02_03: String = result.Data20!!.FBUG1.Comments.faculty
//                                var barcount1c02_03: Float =
//                                    parseFloat(result.Data20!!.FBUG1.Comments.count)
//                                var colorc02_03: String = result.Data20!!.FBUG1.Comments.color
//                                val barEntries1c02_03 = ArrayList<BarEntry>()
//                                barEntries1c02_03.add(BarEntry(2f, barcount1c02_03))
//                                val barDataSet102_03 = BarDataSet(barEntries1c02_03, faculty02_03)
//                                barDataSet102_03.color = Color.parseColor(colorc02_03)
//                                barDataSet102_03.valueTextSize = 15f
//
//                                var faculty02_04: String = result.Data20!!.FBUG1.No_Answer.faculty
//                                var barcount1c02_04: Float =
//                                    parseFloat(result.Data20!!.FBUG1.No_Answer.count)
//                                var colorc02_04: String = result.Data20!!.FBUG1.No_Answer.color
//                                val barEntries1c02_04 = ArrayList<BarEntry>()
//                                barEntries1c02_04.add(BarEntry(2f, barcount1c02_04))
//                                val barDataSet102_04 = BarDataSet(barEntries1c02_04, faculty02_04)
//                                barDataSet102_04.color = Color.parseColor(colorc02_04)
//                                barDataSet102_04.valueTextSize = 15f
//
//                                val data02 =
//                                    BarData(
//                                        barDataSet102_01,
//                                        barDataSet102_02,
//                                        barDataSet102_03,
//                                        barDataSet102_04
//                                    )
//                                /*----------------------------------------------------------------*/
//                                mpBarChart1.data = data
//                                mpBarChart1.isHighlightFullBarEnabled
//                                mpBarChart1.getDescription().setEnabled(false);
//                                val xAxis1 = mpBarChart1.xAxis
//                                val Axis_right1 = mpBarChart1.axisRight
//                                Axis_right1.isEnabled = false
//
//                                xAxis1.textSize = 0.1f
//                                xAxis1.isEnabled = false
//                                xAxis1.setCenterAxisLabels(true)
//                                xAxis1.position = XAxis.XAxisPosition.TOP
//                                xAxis1.granularity = 1f
//                                xAxis1.isGranularityEnabled = true
//                                mpBarChart1.isDragEnabled = true
//                                mpBarChart1.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
//                                data.barWidth = 0.05f
//                                mpBarChart1.xAxis.axisMinimum = 0f
//                                mpBarChart1.xAxis.axisMaximum = 1f
//                                mpBarChart1.axisLeft.axisMinimum = 0f
//                                mpBarChart1.groupBars(0.0f, 0.00f, 0.05f)
//                                mpBarChart1.invalidate()
//                                txt_barchart1.setText("Name of Faculty")
//                                /*----------------------------------------------------------------*/
//                                mpBarChart2.data = data02
//                                mpBarChart2.isHighlightFullBarEnabled
//                                mpBarChart2.getDescription().setEnabled(false);
//                                val xAxis2 = mpBarChart2.xAxis
//                                val Axis_right2 = mpBarChart2.axisRight
//                                Axis_right2.isEnabled = false
//
//                                xAxis2.textSize = 0.1f
//                                xAxis2.isEnabled = false
//                                xAxis2.setCenterAxisLabels(true)
//                                xAxis2.position = XAxis.XAxisPosition.TOP
//                                xAxis2.granularity = 1f
//                                xAxis2.isGranularityEnabled = true
//                                mpBarChart2.isDragEnabled = true
//                                mpBarChart2.setVisibleXRangeMaximum(2f)
////                                val barSpace = 0.01f
////                                val groupSpace = 0.00f
//                                data02.barWidth = 0.05f
//                                mpBarChart2.xAxis.axisMinimum = 0f
//                                mpBarChart2.xAxis.axisMaximum = 1f
//                                mpBarChart2.axisLeft.axisMinimum = 0f
//                                mpBarChart2.groupBars(0.0f, 0.00f, 0.05f)
//                                mpBarChart2.invalidate()
//                                txt_barchart2.setText("Summary of FBUG 1\nExamination")

                            } else {
                                GenericUserFunction.showApiError(
                                    this@FeedbackAnalysisRecycle,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )

                            }
                        }

                    }

//                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
//                    PhpApiInterface::class.java
//                )
//                var call2: Call<GetGraphList> = phpApiInterface.GetGraphData()
//                call2.enqueue(object : Callback<GetGraphList> {
//                    override fun onFailure(call: Call<GetGraphList>, t: Throwable) {
//                        Toast.makeText(this@FeedbackAnalysis1, t.message, Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    }
//
//                    override fun onResponse(
//                        call: Call<GetGraphList>,
//                        response: Response<GetGraphList>
//                    ) {
//                        val result: GetGraphList? = response.body()
//                        println("result 1 >>> " + result!!.Data!![0])
//                        var listSize = result.Data!!.size
//                        if (result!!.Data!!.isEmpty()) {
//
//                        } else {
//                            if (listSize != 0) {
//                                dialog.dismiss()
//                                var faculty: String = result.Data!![0].faculty
//                                var barcount1: Float =
//                                    parseFloat(result.Data!![0].count)
//                                var color: String = result.Data!![0].colorcode
//                                val barEntries1 = ArrayList<BarEntry>()
//                                barEntries1.add(BarEntry(1f, barcount1))
//
//                                val barDataSet0 = BarDataSet(barEntries1, faculty)
//                                barDataSet0.color = Color.parseColor(color)
//                                barDataSet0.valueTextSize = 15f
//
//                                var facultyc: String = result.Data!![1].faculty
//                                var barcount1c: Float = parseFloat(result.Data!![1].count)
//                                var colorc: String = result.Data!![1].colorcode
//                                val barEntries1c = ArrayList<BarEntry>()
//                                barEntries1c.add(BarEntry(2f, barcount1c))
//                                val barDataSet1 = BarDataSet(barEntries1c, facultyc)
//                                barDataSet1.color = Color.parseColor(colorc)
//                                barDataSet1.valueTextSize = 15f
//
//
//                                var faculty2: String = result.Data!![2].faculty
//                                var barcount12: Float =
//                                    parseFloat(result.Data!![2].count)
//                                var color2: String = result.Data!![2].colorcode
//                                val barEntries12 = ArrayList<BarEntry>()
//                                barEntries12.add(BarEntry(2f, barcount12))
//
//                                val barDataSet2 = BarDataSet(barEntries12, faculty2)
//                                barDataSet2.color = Color.parseColor(color2)
//                                barDataSet2.valueTextSize = 15f
//
//                                var faculty3: String = result.Data!![3].faculty
//                                var barcount3: Float =
//                                    parseFloat(result.Data!![3].count)
//                                var color3: String = result.Data!![3].colorcode
//                                val barEntries3 = ArrayList<BarEntry>()
//                                barEntries3.add(BarEntry(2f, barcount3))
//
//                                val barDataSet3 = BarDataSet(barEntries3, faculty3)
//                                barDataSet3.color = Color.parseColor(color3)
//                                barDataSet3.valueTextSize = 15f
//
//                                var faculty4: String = result.Data!![4].faculty
//                                var barcount14: Float =
//                                    parseFloat(result.Data!![4].count)
//                                var color4: String = result.Data!![4].colorcode
//                                val barEntries14 = ArrayList<BarEntry>()
//                                barEntries14.add(BarEntry(2f, barcount14))
//
//                                val barDataSet4 = BarDataSet(barEntries14, faculty4)
//                                barDataSet4.color = Color.parseColor(color4)
//                                barDataSet4.valueTextSize = 15f
//
//                                var faculty5: String = result.Data!![5].faculty
//                                var barcount15: Float =
//                                    parseFloat(result.Data!![5].count)
//                                var color5: String = result.Data!![5].colorcode
//                                val barEntries15 = ArrayList<BarEntry>()
//                                barEntries15.add(BarEntry(2f, barcount15))
//
//                                val barDataSet5 = BarDataSet(barEntries15, faculty5)
//                                barDataSet5.color = Color.parseColor(color5)
//                                barDataSet5.valueTextSize = 15f
//
//                                val data =
//                                    BarData(barDataSet0,barDataSet1, barDataSet2,barDataSet3,barDataSet4,barDataSet5)
///*----------------------------------------------------------------*/
//                                mpBarChart1.data = data
//                                mpBarChart1.isHighlightFullBarEnabled
//                                val xAxis1 = mpBarChart1.xAxis
//                                val Axis_right1=mpBarChart1.axisRight
//                                Axis_right1.isEnabled=false
//
//                                xAxis1.textSize = 0.1f
//                                xAxis1.isEnabled=false
//                                xAxis1.setCenterAxisLabels(true)
//                                xAxis1.position = XAxis.XAxisPosition.TOP
//                                xAxis1.granularity = 1f
//                                xAxis1.isGranularityEnabled = true
//                                mpBarChart1.isDragEnabled = true
//                                mpBarChart1.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
//                                data.barWidth = 0.16f
//                                mpBarChart1.xAxis.axisMinimum = 0f
//                                mpBarChart1.xAxis.axisMaximum = 1f
//                                mpBarChart1.axisLeft.axisMinimum = 0f
//                                mpBarChart1.groupBars(0.0f, 0.00f, 0.01f)
//                                mpBarChart1.invalidate()
//                                /*----------------------------------------------------------------*/
//                                mpBarChart2.data = data
//                                mpBarChart2.isHighlightFullBarEnabled
//                                val xAxis2 = mpBarChart2.xAxis
//                                xAxis2.textSize = 0.1f
//                                xAxis2.isEnabled=false
//                                val Axis_right2=mpBarChart2.axisRight
//                                Axis_right2.isEnabled=false
//
//                                xAxis2.setCenterAxisLabels(true)
//                                xAxis2.position = XAxis.XAxisPosition.TOP
//                                xAxis2.granularity = 1f
//                                xAxis2.isGranularityEnabled = true
//                                mpBarChart2.isDragEnabled = true
//                                mpBarChart2.setVisibleXRangeMaximum(2f)
////                                val barSpace = 0.01f
////                                val groupSpace = 0.00f
//                                data.barWidth = 0.16f
//                                mpBarChart2.xAxis.axisMinimum = 0f
//                                mpBarChart2.xAxis.axisMaximum = 1f
//                                mpBarChart2.axisLeft.axisMinimum = 0f
//                                mpBarChart2.groupBars(0.0f, 0.00f, 0.01f)
//                                mpBarChart2.invalidate()
//                                /*----------------------------------------------------------------*/
//                                mpBarChart3.data = data
//                                mpBarChart3.isHighlightFullBarEnabled
//                                val xAxis3 = mpBarChart3.xAxis
//                                xAxis3.textSize = 10f
//                                xAxis3.isEnabled=false
//                                val Axis_right3=mpBarChart3.axisRight
//                                Axis_right3.isEnabled=false
//
//                                xAxis3.setCenterAxisLabels(true)
//                                xAxis3.position = XAxis.XAxisPosition.TOP
//                                xAxis3.granularity = 1f
//                                xAxis3.isGranularityEnabled = true
//                                mpBarChart3.isDragEnabled = true
//                                mpBarChart3.setVisibleXRangeMaximum(2f)
////                                val barSpace = 0.01f
////                                val groupSpace = 0.00f
//                                data.barWidth = 0.16f
//                                mpBarChart3.xAxis.axisMinimum = 0f
//                                mpBarChart3.xAxis.axisMaximum = 1f
//                                mpBarChart3.axisLeft.axisMinimum = 0f
//                                mpBarChart3.groupBars(0.0f, 0.00f, 0.01f)
//                                mpBarChart3.invalidate()
//                                /*----------------------------------------------------------------*/
//                                mpBarChart4.data = data
//                                mpBarChart4.isHighlightFullBarEnabled
//                                val xAxis4 = mpBarChart4.xAxis
//                                xAxis4.textSize = 10f
//                                xAxis4.isEnabled=false
//                                val Axis_right4=mpBarChart4.axisRight
//                                Axis_right4.isEnabled=false
//
//                                xAxis4.setCenterAxisLabels(true)
//                                xAxis4.position = XAxis.XAxisPosition.TOP
//                                xAxis4.granularity = 1f
//                                xAxis4.isGranularityEnabled = true
//                                mpBarChart4.isDragEnabled = true
//                                mpBarChart4.setVisibleXRangeMaximum(2f)
////                                val barSpace = 0.01f
////                                val groupSpace = 0.00f
//                                data.barWidth = 0.16f
//                                mpBarChart4.xAxis.axisMinimum = 0f
//                                mpBarChart4.xAxis.axisMaximum = 1f
//                                mpBarChart4.axisLeft.axisMinimum = 0f
//                                mpBarChart4.groupBars(0.0f, 0.00f, 0.01f)
//                                mpBarChart4.invalidate()
//                                /*----------------------------------------------------------------*/
//                                mpBarChart5.data = data
//                                mpBarChart5.isHighlightFullBarEnabled
//                                val xAxis5 = mpBarChart5.xAxis
//                                xAxis5.textSize = 10f
//                                xAxis5.isEnabled=false
//                                val Axis_right5=mpBarChart5.axisRight
//                                Axis_right5.isEnabled=false
//
//                                xAxis5.setCenterAxisLabels(true)
//                                xAxis5.position = XAxis.XAxisPosition.TOP
//                                xAxis5.granularity = 1f
//                                xAxis5.isGranularityEnabled = true
//                                mpBarChart5.isDragEnabled = true
//                                mpBarChart5.setVisibleXRangeMaximum(2f)
////                                val barSpace = 0.01f
////                                val groupSpace = 0.00f
//                                data.barWidth = 0.16f
//                                mpBarChart5.xAxis.axisMinimum = 0f
//                                mpBarChart5.xAxis.axisMaximum = 1f
//                                mpBarChart5.axisLeft.axisMinimum = 0f
//                                mpBarChart5.groupBars(0.0f, 0.00f, 0.01f)
//                                mpBarChart5.invalidate()
//                                /*----------------------------------------------------------------*/
//
//
//                            } else {
//                                if (listSize == 0) {
//                                    dialog.dismiss()
//                                    var msg = "No data available for analysis"
//                                    GenericPublicVariable.CustDialog =
//                                        Dialog(this@FeedbackAnalysis1)
//                                    GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                    var ivNegClose1: ImageView =
//                                        GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                    var btnOk: Button =
//                                        GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                    var tvMsg: TextView =
//                                        GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                    tvMsg.text = msg
//                                    GenericPublicVariable.CustDialog.setCancelable(false)
//                                    btnOk.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                        onBackPressed()
//
//                                    }
//                                    ivNegClose1.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                        onBackPressed()
//                                    }
//                                    GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
//                                        ColorDrawable(Color.TRANSPARENT)
//                                    )
//                                    GenericPublicVariable.CustDialog.show()
//                                }
//
//                            }
//                        }
//                        {UserName
//                            var listSize = result.Data14!!.size
//                            val users = ArrayList<McqFields>()
//                            println("result 4>>> " + users)
//
//                                for (i in 0..listSize - 1) {
//                                    if (result.Data14!![i].STUDENT_FLAG == "T") {
//                                        if (result.Data14!![i].COURSE_ID == "All" || result.Data14!![i].COURSE_ID == COURSE_ID) {
//                                            if (result.Data14!![i].INSTITUTE_NAME == instname || result.Data14!![i].INSTITUTE_NAME == "ADMIN") {
//
//                                                if (result.Data14!![i].RESOU_FLAG == "T") {
//                                                    k = R.drawable.ic_notice_yes
//                                                } else {
//                                                    k = R.drawable.ic_anotice_no
//                                                }
//                                                users.add(
//                                                    McqFields(
//                                                        result.Data14!![i].NOTICE_TITLE,
//                                                        result.Data14!![i].USER_ROLE,
//                                                        result.Data14!![i].USER_TYPE,
//                                                        result.Data14!![i].NOTICE_TYPE,
//                                                        result.Data14!![i].NOTICE_DESC,
//                                                        result.Data14!![i].NOTICE_DATE,
//                                                        result.Data14!![i].INSTITUTE_NAME,
//                                                        result.Data14!![i].COURSE_NAME,
//                                                        result.Data14!![i].COURSE_ID,
//                                                        result.Data14!![i].DEPT_NAME,
//                                                        result.Data14!![i].DEPT_ID,
//                                                        result.Data14!![i].RESOU_FLAG,
//                                                        result.Data14!![i].FILENAME,
//                                                        k
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            progressBar!!.visibility = View.INVISIBLE
//                            progressBar!!.visibility = View.GONE
//                            if(users.isEmpty()){
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            }else {
//
//                                val adapter = ExamGetMCQAdapter(users)
//                                recyclerView.adapter = adapter
//                            }
//
//                        }
//                        else
//                        {
//                            if (result.Status.equals("No data found", ignoreCase = true)) {
//
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            } else {
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//                                println("result 3>>>" + result.Status)
//                                Toast.makeText(
//                                    this@EXAM_GET_UploadMCQ,
//                                    result.Status,
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//                        }
//                    }
//
//                }
//                }


//            {
//                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//
//
//
//                    }
//
//                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
//                         }

                    )

            } catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
    }

    fun getGraphData() {
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are updating Data for Graph")
            dialog.setCancelable(false)
            dialog.show()
            try {
                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                    PhpApiInterface::class.java
                )
                var call2: Call<GetGraphList> = phpApiInterface.GetGraphData()
                call2.enqueue(object : Callback<GetGraphList> {
                    override fun onFailure(call: Call<GetGraphList>, t: Throwable) {
                        Toast.makeText(this@FeedbackAnalysisRecycle, t.message, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    override fun onResponse(
                        call: Call<GetGraphList>,
                        response: Response<GetGraphList>
                    ) {
                        val result: GetGraphList? = response.body()
                        println("result 1 >>> " + result!!.Data!![0])
                        var listSize = result.Data!!.size
                        if (result!!.Data!!.isEmpty()) {

                        } else {
                            if (listSize != 0) {
                                dialog.dismiss()
                                var faculty: String = result.Data!![0].faculty
                                var barcount1: Float =
                                    parseFloat(result.Data!![0].count)
                                var color: String = result.Data!![0].colorcode
                                val barEntries1 = ArrayList<BarEntry>()
                                barEntries1.add(BarEntry(1f, barcount1))

                                val barDataSet0 = BarDataSet(barEntries1, faculty)
                                barDataSet0.color = Color.parseColor(color)
                                barDataSet0.valueTextSize = 15f

                                var facultyc: String = result.Data!![1].faculty
                                var barcount1c: Float = parseFloat(result.Data!![1].count)
                                var colorc: String = result.Data!![1].colorcode
                                val barEntries1c = ArrayList<BarEntry>()
                                barEntries1c.add(BarEntry(2f, barcount1c))
                                val barDataSet1 = BarDataSet(barEntries1c, facultyc)
                                barDataSet1.color = Color.parseColor(colorc)
                                barDataSet1.valueTextSize = 15f


                                var faculty2: String = result.Data!![2].faculty
                                var barcount12: Float =
                                    parseFloat(result.Data!![2].count)
                                var color2: String = result.Data!![2].colorcode
                                val barEntries12 = ArrayList<BarEntry>()
                                barEntries12.add(BarEntry(2f, barcount12))

                                val barDataSet2 = BarDataSet(barEntries12, faculty2)
                                barDataSet2.color = Color.parseColor(color2)
                                barDataSet2.valueTextSize = 15f

                                var faculty3: String = result.Data!![3].faculty
                                var barcount3: Float =
                                    parseFloat(result.Data!![3].count)
                                var color3: String = result.Data!![3].colorcode
                                val barEntries3 = ArrayList<BarEntry>()
                                barEntries3.add(BarEntry(2f, barcount3))

                                val barDataSet3 = BarDataSet(barEntries3, faculty3)
                                barDataSet3.color = Color.parseColor(color3)
                                barDataSet3.valueTextSize = 15f

                                var faculty4: String = result.Data!![4].faculty
                                var barcount14: Float =
                                    parseFloat(result.Data!![4].count)
                                var color4: String = result.Data!![4].colorcode
                                val barEntries14 = ArrayList<BarEntry>()
                                barEntries14.add(BarEntry(2f, barcount14))

                                val barDataSet4 = BarDataSet(barEntries14, faculty4)
                                barDataSet4.color = Color.parseColor(color4)
                                barDataSet4.valueTextSize = 15f

                                var faculty5: String = result.Data!![5].faculty
                                var barcount15: Float =
                                    parseFloat(result.Data!![5].count)
                                var color5: String = result.Data!![5].colorcode
                                val barEntries15 = ArrayList<BarEntry>()
                                barEntries15.add(BarEntry(2f, barcount15))

                                val barDataSet5 = BarDataSet(barEntries15, faculty5)
                                barDataSet5.color = Color.parseColor(color5)
                                barDataSet5.valueTextSize = 15f

                                val data =
                                    BarData(
                                        barDataSet0,
                                        barDataSet1,
                                        barDataSet2,
                                        barDataSet3,
                                        barDataSet4,
                                        barDataSet5
                                    )
/*----------------------------------------------------------------*/
                                mpBarChart1.data = data
                                mpBarChart1.isHighlightFullBarEnabled
                                val xAxis1 = mpBarChart1.xAxis
                                val Axis_right1 = mpBarChart1.axisRight
                                Axis_right1.isEnabled = false

                                xAxis1.textSize = 0.1f
                                xAxis1.isEnabled = false
                                xAxis1.setCenterAxisLabels(true)
                                xAxis1.position = XAxis.XAxisPosition.TOP
                                xAxis1.granularity = 1f
                                xAxis1.isGranularityEnabled = true
                                mpBarChart1.isDragEnabled = true
                                mpBarChart1.setVisibleXRangeMaximum(2f)
                                val barSpace = 0.01f
                                val groupSpace = 0.00f
                                data.barWidth = 0.16f
                                mpBarChart1.xAxis.axisMinimum = 0f
                                mpBarChart1.xAxis.axisMaximum = 1f
                                mpBarChart1.axisLeft.axisMinimum = 0f
                                mpBarChart1.groupBars(0.0f, 0.00f, 0.01f)
                                mpBarChart1.invalidate()
                                /*----------------------------------------------------------------*/
                                mpBarChart2.data = data
                                mpBarChart2.isHighlightFullBarEnabled
                                val xAxis2 = mpBarChart2.xAxis
                                xAxis2.textSize = 0.1f
                                xAxis2.isEnabled = false
                                val Axis_right2 = mpBarChart2.axisRight
                                Axis_right2.isEnabled = false

                                xAxis2.setCenterAxisLabels(true)
                                xAxis2.position = XAxis.XAxisPosition.TOP
                                xAxis2.granularity = 1f
                                xAxis2.isGranularityEnabled = true
                                mpBarChart2.isDragEnabled = true
                                mpBarChart2.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
                                data.barWidth = 0.16f
                                mpBarChart2.xAxis.axisMinimum = 0f
                                mpBarChart2.xAxis.axisMaximum = 1f
                                mpBarChart2.axisLeft.axisMinimum = 0f
                                mpBarChart2.groupBars(0.0f, 0.00f, 0.01f)
                                mpBarChart2.invalidate()
                                /*----------------------------------------------------------------*/
                                mpBarChart3.data = data
                                mpBarChart3.isHighlightFullBarEnabled
                                val xAxis3 = mpBarChart3.xAxis
                                xAxis3.textSize = 10f
                                xAxis3.isEnabled = false
                                val Axis_right3 = mpBarChart3.axisRight
                                Axis_right3.isEnabled = false

                                xAxis3.setCenterAxisLabels(true)
                                xAxis3.position = XAxis.XAxisPosition.TOP
                                xAxis3.granularity = 1f
                                xAxis3.isGranularityEnabled = true
                                mpBarChart3.isDragEnabled = true
                                mpBarChart3.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
                                data.barWidth = 0.16f
                                mpBarChart3.xAxis.axisMinimum = 0f
                                mpBarChart3.xAxis.axisMaximum = 1f
                                mpBarChart3.axisLeft.axisMinimum = 0f
                                mpBarChart3.groupBars(0.0f, 0.00f, 0.01f)
                                mpBarChart3.invalidate()
                                /*----------------------------------------------------------------*/
                                mpBarChart4.data = data
                                mpBarChart4.isHighlightFullBarEnabled
                                val xAxis4 = mpBarChart4.xAxis
                                xAxis4.textSize = 10f
                                xAxis4.isEnabled = false
                                val Axis_right4 = mpBarChart4.axisRight
                                Axis_right4.isEnabled = false

                                xAxis4.setCenterAxisLabels(true)
                                xAxis4.position = XAxis.XAxisPosition.TOP
                                xAxis4.granularity = 1f
                                xAxis4.isGranularityEnabled = true
                                mpBarChart4.isDragEnabled = true
                                mpBarChart4.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
                                data.barWidth = 0.16f
                                mpBarChart4.xAxis.axisMinimum = 0f
                                mpBarChart4.xAxis.axisMaximum = 1f
                                mpBarChart4.axisLeft.axisMinimum = 0f
                                mpBarChart4.groupBars(0.0f, 0.00f, 0.01f)
                                mpBarChart4.invalidate()
                                /*----------------------------------------------------------------*/
                                mpBarChart5.data = data
                                mpBarChart5.isHighlightFullBarEnabled
                                val xAxis5 = mpBarChart5.xAxis
                                xAxis5.textSize = 10f
                                xAxis5.isEnabled = false
                                val Axis_right5 = mpBarChart5.axisRight
                                Axis_right5.isEnabled = false

                                xAxis5.setCenterAxisLabels(true)
                                xAxis5.position = XAxis.XAxisPosition.TOP
                                xAxis5.granularity = 1f
                                xAxis5.isGranularityEnabled = true
                                mpBarChart5.isDragEnabled = true
                                mpBarChart5.setVisibleXRangeMaximum(2f)
//                                val barSpace = 0.01f
//                                val groupSpace = 0.00f
                                data.barWidth = 0.16f
                                mpBarChart5.xAxis.axisMinimum = 0f
                                mpBarChart5.xAxis.axisMaximum = 1f
                                mpBarChart5.axisLeft.axisMinimum = 0f
                                mpBarChart5.groupBars(0.0f, 0.00f, 0.01f)
                                mpBarChart5.invalidate()
                                /*----------------------------------------------------------------*/


                            } else {
                                if (listSize == 0) {
                                    dialog.dismiss()
                                    var msg = "No data available for analysis"
                                    GenericPublicVariable.CustDialog =
                                        Dialog(this@FeedbackAnalysisRecycle)
                                    GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
                                    var ivNegClose1: ImageView =
                                        GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                                    var btnOk: Button =
                                        GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                    var tvMsg: TextView =
                                        GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                    tvMsg.text = msg
                                    GenericPublicVariable.CustDialog.setCancelable(false)
                                    btnOk.setOnClickListener {
                                        GenericPublicVariable.CustDialog.dismiss()
                                        onBackPressed()

                                    }
                                    ivNegClose1.setOnClickListener {
                                        GenericPublicVariable.CustDialog.dismiss()
                                        onBackPressed()
                                    }
                                    GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                        ColorDrawable(Color.TRANSPARENT)
                                    )
                                    GenericPublicVariable.CustDialog.show()
                                }

                            }
                        }
//                        {UserName
//                            var listSize = result.Data14!!.size
//                            val users = ArrayList<McqFields>()
//                            println("result 4>>> " + users)
//
//                                for (i in 0..listSize - 1) {
//                                    if (result.Data14!![i].STUDENT_FLAG == "T") {
//                                        if (result.Data14!![i].COURSE_ID == "All" || result.Data14!![i].COURSE_ID == COURSE_ID) {
//                                            if (result.Data14!![i].INSTITUTE_NAME == instname || result.Data14!![i].INSTITUTE_NAME == "ADMIN") {
//
//                                                if (result.Data14!![i].RESOU_FLAG == "T") {
//                                                    k = R.drawable.ic_notice_yes
//                                                } else {
//                                                    k = R.drawable.ic_anotice_no
//                                                }
//                                                users.add(
//                                                    McqFields(
//                                                        result.Data14!![i].NOTICE_TITLE,
//                                                        result.Data14!![i].USER_ROLE,
//                                                        result.Data14!![i].USER_TYPE,
//                                                        result.Data14!![i].NOTICE_TYPE,
//                                                        result.Data14!![i].NOTICE_DESC,
//                                                        result.Data14!![i].NOTICE_DATE,
//                                                        result.Data14!![i].INSTITUTE_NAME,
//                                                        result.Data14!![i].COURSE_NAME,
//                                                        result.Data14!![i].COURSE_ID,
//                                                        result.Data14!![i].DEPT_NAME,
//                                                        result.Data14!![i].DEPT_ID,
//                                                        result.Data14!![i].RESOU_FLAG,
//                                                        result.Data14!![i].FILENAME,
//                                                        k
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            progressBar!!.visibility = View.INVISIBLE
//                            progressBar!!.visibility = View.GONE
//                            if(users.isEmpty()){
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            }else {
//
//                                val adapter = ExamGetMCQAdapter(users)
//                                recyclerView.adapter = adapter
//                            }
//
//                        }
//                        else
//                        {
//                            if (result.Status.equals("No data found", ignoreCase = true)) {
//
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            } else {
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//                                println("result 3>>>" + result.Status)
//                                Toast.makeText(
//                                    this@EXAM_GET_UploadMCQ,
//                                    result.Status,
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//                        }
                    }
//
//                }
                }


//            {
//                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//
//
//
//                    }
//
//                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
//                         }

                )

            } catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
    }

    fun getGraphDatas() {
//        if (InternetConnection.checkConnection(this)) {
//            val dialog: android.app.AlertDialog =
//                SpotsDialog.Builder().setContext(this).build()
//            dialog.setMessage("Please Wait!!! \nwhile we delete ExamKey")
//            dialog.setCancelable(false)
//            dialog.show()
//            try {
//                mServices.GetGraph("2019")
//                    .enqueue(object : Callback<APIResponse> {
//                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                            dialog.dismiss()
//                            Toast.makeText(this@FeedbackAnalysis1, t.message, Toast.LENGTH_SHORT).show()
//
//
//                        }
//
//                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
//                            val result: APIResponse? = response.body()
//                            println("result 1 >>> "+result.toString())
//                            if (result!!.Status == "ok") {
//                                var listSize = result.Data14!!.size
//                                val users = ArrayList<NoticeStudCurrent>()
//                                println("result 4>>> "+users)
//
//                                for (i in 0..listSize - 1) {
//                                    if (result.Data14!![i].ADMIN_FLAG == "T") {
//                                        // if (result!!.Data14!![i].COURSE_ID == "All" || result!!.Data14!![i].COURSE_ID == COURSE_ID) {
//                                        if (result.Data14!![i].RESOU_FLAG == "T") {
////                                        k = R.drawable.ic_notice_yes
//                                            result.Data14!![i].ID
//
//                                            if(result.Data14!![i].FILENAME.contains(".jpg",ignoreCase = true)||result.Data14!![i].FILENAME.contains(".jpeg",ignoreCase = true)||result.Data14!![i].FILENAME.contains(".png",ignoreCase = true))
//                                            {
//                                                k = R.drawable.ic_jpg
//                                            }else
//                                                if(result.Data14!![i].FILENAME.contains(".pdf",ignoreCase = true)) {
//                                                    k = R.drawable.icon_pdf
//                                                }
////                                        ???
//
//                                        } else {
//                                            k = R.drawable.ic_anotice_no
//                                        }
//
//
//                                        users.add(
//                                            NoticeStudCurrent(
//                                                "Notice Title : " + result.Data14!![i].NOTICE_TITLE,
//                                                "Sender : " + result.Data14!![i].USER_ROLE,
//                                                "Notice For : " + result.Data14!![i].USER_TYPE,
//                                                "Notice Type : " + result.Data14!![i].NOTICE_TYPE,
//                                                "Notice Desc : " + result.Data14!![i].NOTICE_DESC,
//                                                "Notice Date : " + result.Data14!![i].NOTICE_DATE,
//                                                "Institute : " + result.Data14!![i].INSTITUTE_NAME,
//                                                "Course Name : " + result.Data14!![i].COURSE_NAME,
//                                                "Course ID : " + result.Data14!![i].COURSE_ID,
//                                                "Dept Name : " + result.Data14!![i].DEPT_NAME,
//                                                "Dept ID : " + result.Data14!![i].DEPT_ID,
//                                                "ATTACHMENT STATUS: " + result.Data14!![i].RESOU_FLAG,
//                                                result.Data14!![i].FILENAME,
//                                                result.Data14!![i].YEAR,
//
//                                                result.Data14!![i].STUDENT_FLAG,
//                                                result.Data14!![i].FACULTY_FLAG,
//                                                result.Data14!![i].ADMIN_FLAG,
//                                                k
//                                            )
//                                        )
//
//                                    }
//                                }
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar.visibility = View.GONE
//                                if(users.isEmpty())
//                                {
//                                    GenericUserFunction.showOopsError(
//                                        this@FeedbackAnalysis1,
//                                        "No Notices found for the current request"
//                                    )
//                                }else {
//                                    val adapter = NoticeAdapterCurrent(users, this@FeedbackAnalysis1)
//                                    recyclerView.adapter = adapter
//                                }
//
//                            }
//                            else {
//                                if (result.Status.equals("No data found", ignoreCase = true)) {
//
//                                    progressBar!!.visibility = View.INVISIBLE
//                                    progressBar!!.visibility = View.GONE
//
//                                    var msg="No Notices found for the current request"
//                                    GenericPublicVariable.CustDialog = Dialog(this@FeedbackAnalysis1)
//                                    GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                    var ivNegClose1: ImageView =
//                                        GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                    var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                    var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                    tvMsg.text = msg
//                                    GenericPublicVariable.CustDialog.setCancelable(false)
//                                    btnOk.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                        onBackPressed()
//
//                                    }
//                                    ivNegClose1.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                        onBackPressed()
//                                    }
//                                    GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                    GenericPublicVariable.CustDialog.show()
//                                } else {
//                                    progressBar.visibility = View.INVISIBLE
//                                    progressBar.visibility = View.GONE
//                                    println("result 3>>>" + result.Status)
//                                    Toast.makeText(
//                                        this@FeedbackAnalysis1,
//                                        result.Status,
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                }
//                            }
//                        }
//                    })
//
//            }catch (ex: Exception) {
//
//                ex.printStackTrace()
//                GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
//            }
//        }
//        else {
//            GenericUserFunction.showInternetNegativePopUp(
//                this,
//                getString(R.string.failureNoInternetErr)
//            )
//        }
    }
}
