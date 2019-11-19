package com.dmims.dmims.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebViewClient
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.github.barteksc.pdfviewer.PDFView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import dmax.dialog.SpotsDialog
import java.io.File
import com.krishna.fileloader.listener.FileRequestListener as FileRequestListener1

class StudentPdfViewr : AppCompatActivity() {
    var pdf: PDFView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_pdf_viewr)
        var fileurl: String = intent.getStringExtra("pdfUrl")
        pdf = findViewById<PDFView>(R.id.pdfid)
        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
        try{
            dialog.setMessage("Please Wait!!! \nwhile we are updating your Time Table")
            dialog.setCancelable(false)
            dialog.show()
       FileLoader.with(this)
           .load(fileurl)
           .fromDirectory("PDFFiles",FileLoader.DIR_EXTERNAL_PUBLIC)
           .asFile(object : com.krishna.fileloader.listener.FileRequestListener<File> {
               override fun onLoad(request: FileLoadRequest?, response: FileResponse<File>?) {
                   dialog.dismiss()
                   var pdfFile:File = response!!.body
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
                        Toast.makeText(this@StudentPdfViewr,"Error while open page",Toast.LENGTH_SHORT) }
                    .onPageChange { page, pageCount ->
                        println(page)
                        println(pageCount)


                    }
                    .onTap {
                        return@onTap true
                    }.onRender { nbPages, pageWidth, pageHeight ->
                        println(pageHeight)
                        println(pageWidth)
                        println(nbPages)
                        pdf!!.fitToWidth()
                    }
                    .enableAnnotationRendering(true)
                    .invalidPageColor(Color.WHITE)
                    .load()

               }
               override fun onError(request: FileLoadRequest?, t: Throwable?) {
                   dialog.dismiss()
                Toast.makeText(this@StudentPdfViewr,t!!.message.toString(),Toast.LENGTH_SHORT).show()
               }
           })
    }catch (ex: Exception) {
            dialog.dismiss()
        ex.printStackTrace()
        GenericUserFunction.showApiError(
            this,
            "Sorry for inconvenience\nUnable to view PDF."
        )
    }
    }
}
