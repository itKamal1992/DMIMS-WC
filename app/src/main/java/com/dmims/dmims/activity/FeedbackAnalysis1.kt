package com.dmims.dmims.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dmims.dmims.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList

class FeedbackAnalysis1 : AppCompatActivity() {

    lateinit var mpBarChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_analysis1)


        mpBarChart = findViewById(R.id.barchart3)

        val barEntries3 = ArrayList<BarEntry>()
        barEntries3.add(BarEntry(3f, 0f))


        val barEntries4 = ArrayList<BarEntry>()
        barEntries4.add(BarEntry(4f, 0f))


        val barEntries5 = ArrayList<BarEntry>()
        barEntries5.add(BarEntry(5f, 0f))

        val barEntries6 = ArrayList<BarEntry>()
        barEntries6.add(BarEntry(6f, 0f))


        val barDataSet1 = BarDataSet(barEntries1(), "0 Medicine")
        barDataSet1.color = Color.parseColor("#2980B9")
        barDataSet1.valueTextSize = 15f


        val barDataSet2 = BarDataSet(barEntries2(), "Dental")
        barDataSet2.color = Color.parseColor("#E74C3C")
        barDataSet2.valueTextSize = 15f


        val barDataSet3 = BarDataSet(barEntries3, "Ayurveda")
        barDataSet3.color = Color.parseColor("#00FF00")
        barDataSet3.valueTextSize = 15f

        val barDataSet4 = BarDataSet(barEntries4, "Nursing")
        barDataSet4.color = Color.parseColor("#F4D03F")
        barDataSet4.valueTextSize = 15f

        val barDataSet5 = BarDataSet(barEntries5, "Inter Disciplinary")
        barDataSet5.color = Color.parseColor("#A2D9CE")
        barDataSet5.valueTextSize = 15f

        val barDataSet6 = BarDataSet(barEntries6, "No answer")
        barDataSet6.color = Color.parseColor("#EDBB99")
        barDataSet6.valueTextSize = 15f


        val data =
            BarData(barDataSet1, barDataSet2)

        mpBarChart.data = data
        mpBarChart.isHighlightFullBarEnabled


        val days =
            arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        val xAxis = mpBarChart.xAxis

        xAxis.textSize = 10f
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        mpBarChart.isDragEnabled = true
        mpBarChart.setVisibleXRangeMaximum(2f)


        val barSpace = 0.01f
        val groupSpace = 0.00f
        data.barWidth = 0.16f

        mpBarChart.xAxis.axisMinimum = 0f
        mpBarChart.xAxis.axisMaximum = 1f
        mpBarChart.axisLeft.axisMinimum = 0f
        //from shift the bars to 0.1 position


        mpBarChart.groupBars(0.1f, 0f, 0f)
        mpBarChart.invalidate()

    }
    private fun barEntries1(): ArrayList<BarEntry>
    {
        val barEntries1 = ArrayList<BarEntry>()
        barEntries1.add(BarEntry(1f, 70f))
        return barEntries1
    }

    private fun barEntries2(): ArrayList<BarEntry>
    {
        val barEntries2 = ArrayList<BarEntry>()
        barEntries2.add(BarEntry(2f, 40f))
        return barEntries2
    }
}
