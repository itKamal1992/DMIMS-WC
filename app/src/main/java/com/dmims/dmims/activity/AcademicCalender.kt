package com.dmims.dmims.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.model.TimeTableData
import com.dmims.dmims.model.TimeTableDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import com.github.barteksc.pdfviewer.PDFView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_academic__calender.*
import kotlinx.android.synthetic.main.content_academic__calender.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.math.log

class AcademicCalender : AppCompatActivity()
{
    private var selectedSession: String=""
    var pdf: PDFView? = null
    private var time_table_url: String = "-"
    private var trsnsdlist: ArrayList<TimeTableDataRef>? = null
    var dialogCommon: android.app.AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_academic__calender)
        setSupportActionBar(toolbar)
        dialogCommon = SpotsDialog.Builder().setContext(this).build()
//        pdf = findViewById<PDFView>(R.id.pdfid)

        spinner_sessionAc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                if (!selectedtypeoftimetbl.equals("--Select Session--")) {
                    selectedSession = selectedtypeoftimetbl
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btn_viewCalender.setOnClickListener {
            if((selectedSession=="")||(selectedSession.contains("Select Session"))){
                Toast.makeText(this,"Please select your session",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else
            {
                GetTimetable()
            }
        }
        
        
        
        
    
    }

    fun downloadpdf(time_table_url1: String) {
        FileLoader.with(this)
            .load(time_table_url1)
            .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
            .asFile(object : com.krishna.fileloader.listener.FileRequestListener<File> {
                override fun onLoad(request: FileLoadRequest?, response: FileResponse<File>?) {
                    var pdfFile: File = response!!.body
                    pdf!!.fromFile(pdfFile)
                        .password(null)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .swipeHorizontal(false)
                        .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                            println(canvas)
                            println(pageWidth)
                            println(pageHeight)
                            println(displayedPage)
                        }
                        .onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->
                            println(canvas)
                            println(pageWidth)
                            println(pageHeight)
                            println(displayedPage)

                        }
                        .onPageError { page, t ->
                            println(page)
                            println(t)
                            Toast.makeText(
                                this@AcademicCalender,
                                "Error while open page",
                                Toast.LENGTH_SHORT
                            )
                        }
                        .onPageChange { page, pageCount ->

                        }
                        .onTap {
                            return@onTap true
                        }.onRender { nbPages, pageWidth, pageHeight ->
                            println(nbPages)
                            println(pageWidth)
                            println(pageHeight)
                            pdf!!.fitToWidth()
                        }
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.WHITE)
                        .load()

                }

                override fun onError(request: FileLoadRequest?, t: Throwable?)
                {
                    Toast.makeText(this@AcademicCalender, t!!.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun GetTimetable()
    {
        if (InternetConnection.checkConnection(this)) {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =phpApiInterface.getAcdTimeTable(selectedSession)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>,responsee: Response<ServerResponse>)
            {
                dialogCommon!!.dismiss()
                val serverResponse3 = responsee.body()
                if (serverResponse3 != null) {
                    if (serverResponse3.success)
                    {
                        var intent: Intent = Intent(this@AcademicCalender, Common_PDF_Viewer::class.java)
                        intent.putExtra("url", serverResponse3.message)
                        intent.putExtra("actionTitle", "Academic Calender")
                        startActivity(intent)
                    }
                    else
                    {
                        dialogCommon!!.dismiss()
                        GenericUserFunction.showOopsError(
                            this@AcademicCalender,
                            serverResponse3.message!!
                        )
                    }
                } else {
                    dialogCommon!!.dismiss()
                    GenericUserFunction.showApiError(
                        this@AcademicCalender,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable)
            {
                dialogCommon!!.dismiss()
                GenericUserFunction.showApiError(
                    this@AcademicCalender,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        })
    }
    else
    {
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr))
    }
    }

}
