package com.dmims.dmims.activity

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.broadCasts.SingleUploadBroadcastReceiver
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class AcademicCalenderUploadA : AppCompatActivity(), SingleUploadBroadcastReceiver.Delegate {
    private var mediaPath: String? = null
    private val TAG1: String = "AndroidUploadService"
    var dialogCommon: android.app.AlertDialog? = null
    val uploadReceiver: SingleUploadBroadcastReceiver = SingleUploadBroadcastReceiver()

    override fun onResume() {
        super.onResume()
        uploadReceiver.register(this)
    }

    override fun onPause() {
        super.onPause()
        uploadReceiver.unregister(this)
    }


    override fun onError(exception: Exception) {
        println("onError >>> " + exception.stackTrace)
        dialogCommon!!.dismiss()
        GenericUserFunction.showApiError(
            this,
            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
        )
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCompleted(serverResponseCode: Int, serverResponseBody: ByteArray) {
        println("onCompleted >>> serverResponseCode >>> " + serverResponseCode + " serverResponseBody >>> " + serverResponseBody)

        val charset = Charsets.UTF_8
        println("onCompleted 1 >>> " + serverResponseBody.toString(charset))

        var filename = serverResponseBody.toString(charset)
        dialogCommon!!.dismiss()
        var jsonObject = JSONObject(filename)
        GenericUserFunction.showPositivePopUp(this, jsonObject.getString("response"))

//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgress(progress: Int) {
        println("onProgress 1 >>> uploadedBytes " + progress)
    }


    override fun onProgress(uploadedBytes: Long, totalBytes: Long) {
        println("onProgress 2 >>> uploadedBytes " + uploadedBytes + " totalBytes >>> " + totalBytes)
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelled() {
        println("onCancelled >>> ")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var btnPickPdf: Button
    private lateinit var btnPublishCal: Button
    var REQUEST_CODE: Int = 0
    var type: String? = null
    private var uri: Uri? = null
    private var confirmStatus = "F"
    var titlename: String = "AC_"
    lateinit var str_spinner: String
    lateinit var spinnerSession: Spinner
    lateinit var str_spinnerSession: String

    lateinit var et_pdfName: TextView
    var pdfName1: String? = null
    var PdfNameHolder: String = ""
    var PdfPathHolder: String = ""
    lateinit var PdfID: String


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_calender_upload)

        dialogCommon = SpotsDialog.Builder().setContext(this).build()

        spinnerSession = findViewById(R.id.spinner_sessionAc)
        et_pdfName = findViewById(R.id.et_pdfname)



        spinnerSession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                if (!selectedtypeoftimetbl.equals("--Select Session--")) {
                    pdfName1 = "CAAC_" + selectedtypeoftimetbl
                    et_pdfName.text = pdfName1
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnPickPdf = findViewById(R.id.btn_pdfChoose)
        btnPickPdf.setOnClickListener {

//            REQUEST_CODE = 200
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.setType("*/*")
//            startActivityForResult(intent, 200)

            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "*/*"
            startActivityForResult(galleryIntent, 300)


        }
        btnPublishCal = findViewById(R.id.btn_uploadCalender)
        btnPublishCal.setOnClickListener {

            CheckValidation()

        }

    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            type = "pdf"
            uri = data!!.data
            PdfPathHolder = getPDFPath(uri!!)
            // println("Uri pdf"+SelectedPDF)


            if (uri.toString().isNotEmpty()) {
                confirmStatus = "T"

            } else {
                confirmStatus = "F"
            }
        }
        else if (requestCode == 300 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            type = "pdf"

            if (selectedImage.toString().isNotEmpty()) {
                confirmStatus = "T"

            } else {
                confirmStatus = "F"
            }
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            println("mediaPath >>> $mediaPath")
            //str1.setText(mediaPath)
            // Set the Image in ImageView for Previewing the Media
            //imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
            cursor.close()

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPDFPath(uri: Uri): String {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun CheckValidation() {
        if (spinnerSession.selectedItem.toString().equals("--Select Session--")) {
            Toast.makeText(this, "Please Select Session", Toast.LENGTH_LONG).show()
        } else if (mediaPath == null) {
            println("no uri")
        } else {
//            PdfUploadFunction()

            uploadFile()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun PdfUploadFunction() {

        // PdfPathHolder = FilePath.getPath(this, uri)

        //  println("PdfNameHolder "+PdfNameHolder+"PdfPathHolder "+PdfPathHolder)


        if (PdfPathHolder == null) {

            Toast.makeText(
                this,
                "Please move your PDF file to internal storage & try again.",
                Toast.LENGTH_LONG
            ).show()

        } else {
            //Dialog Start

            //Dialog Start
            dialogCommon!!.setMessage("Please Wait!!! \nwhile we are Uploading Calender")
            dialogCommon!!.setCancelable(false)
            dialogCommon!!.show()

            try {

                PdfID = UUID.randomUUID().toString()
                uploadReceiver.setDelegate(this)
                uploadReceiver.setUploadID(PdfID)
                str_spinnerSession = spinnerSession.selectedItem.toString()

                MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name", pdfName1)
                    .addParameter("Session", str_spinnerSession)

                    .setNotificationConfig(UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload()


            } catch (exception: Exception) {
                dialogCommon!!.dismiss()

                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadFile() {
        if (InternetConnection.checkConnection(this)) {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Time Table.")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)

        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file",pdfName1, requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), pdfName1)


        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =
            phpApiInterface.uploadAcdCal(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                val serverResponse = response.body()
                if (serverResponse != null) {
                    dialogCommon!!.dismiss()
                    if (serverResponse.success) {

                        GenericUserFunction.showPositivePopUp(this@AcademicCalenderUploadA,serverResponse.message.toString())


                    } else {
                        GenericUserFunction.showNegativePopUp(this@AcademicCalenderUploadA,serverResponse.message.toString())

                    }
                } else {
                    dialogCommon!!.dismiss()
//                    assert(serverResponse != null)
//                    Log.v("Response", serverResponse!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                dialogCommon!!.dismiss()
                GenericUserFunction.showNegativePopUp(this@AcademicCalenderUploadA,t.message.toString())

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

    companion object {

        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/uploadAcademicCalenderA.php"
    }

//    private fun up(){
//        Uri fileUri = ... ;// from a file chooser or a camera intent
//
//// create upload service client
//        FileUploadService service =
//        ServiceGenerator.createService(FileUploadService.class);
//
//// create part for file (photo, video, ...)
//        MultipartBody.Part body = prepareFilePart("photo", fileUri);
//
//// create a map of data to pass along
//        var description:RequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, "description one");
//        //createPartFromString("hello, this is description speaking");
//        RequestBody place = createPartFromString("Magdeburg");
//        RequestBody time = createPartFromString("2016");
//
//        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("description", description);
//        map.put("place", place);
//        map.put("time", time);
//    }
}
