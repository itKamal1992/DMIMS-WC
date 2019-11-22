package com.dmims.dmims.activity

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.broadCasts.SingleUploadBroadcastReceiver
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.model.TimeTableData
import com.dmims.dmims.model.TimeTableDataRef
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
import kotlin.collections.ArrayList

class AcademicCalUploadInsti : AppCompatActivity(), SingleUploadBroadcastReceiver.Delegate {
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
        var jsonObject= JSONObject(filename)
        GenericUserFunction.showPositivePopUp(this,jsonObject.getString("response"))

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

    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_semester: Spinner
    private lateinit var spinner_timetabletype: Spinner
    private lateinit var spinner_yearlist: Spinner

    var typeoftimetbl = ArrayList<String>()


    var instituteName1 = ArrayList<String>()
    var semister = ArrayList<String>()
    var yearlist = ArrayList<String>()
    private var trsnsdlist: java.util.ArrayList<TimeTableDataRef>? = null

    private var selectedInstituteName: String? = null
    private lateinit var selectedtypeoftimetbl: String
    var institute_name: String = ""
    var subtype: String = ""
    var year_tt: String = ""
    var sem_tt: String = ""

    var listsinstz: Int = 0
    var REQUEST_CODE: Int = 0
    var type: String? = null
    private var uri: Uri? = null
    private var confirmStatus = "F"

    private lateinit var btnPickPdf: Button
    private lateinit var btnPublishCal: Button

    var pdfName: String = ""
    var PdfNameHolder: String = ""
    var PdfPathHolder: String = ""
    lateinit var PdfID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_cal_upload_insti)

        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_timetabletype = findViewById(R.id.spinner_subType)
        spinner_yearlist = findViewById(R.id.spinner_yearlist)
        spinner_semester = findViewById(R.id.spinner_semesterlist)
        dialogCommon = SpotsDialog.Builder().setContext(this).build()

        btnPickPdf = findViewById(R.id.btn_pdfChoose)
        btnPickPdf.setOnClickListener {
            REQUEST_CODE = 200
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

        instituteName1.add("Select Institute")

        typeoftimetbl.clear()
        typeoftimetbl.add("Select type")
        typeoftimetbl.add("Clinical")
        typeoftimetbl.add("Theory")
        if (InternetConnection.checkConnection(this)) {

            try {
                GenericPublicVariable.mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(
                                this@AcademicCalUploadInsti,
                                t.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(
                                    this@AcademicCalUploadInsti,
                                    result.Status,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                listsinstz = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    instituteName1.add(result.Data6!![i].Course_Institute)
                                }
                            }
                        }
                    })
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }else
        {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
        var institueAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@AcademicCalUploadInsti,
            R.layout.support_simple_spinner_dropdown_item, instituteName1
        )
        spinner_institue.adapter = institueAdap

        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                institute_name = spinner_institue.selectedItem.toString()
            }
        }


        var typeoftimetblAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeoftimetbl)
        spinner_timetabletype.adapter = typeoftimetblAdap

        spinner_timetabletype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@AcademicCalUploadInsti)) {
                try {

                    selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                    if (selectedtypeoftimetbl == "Theory") {
                        selectedtypeoftimetbl = "TH"
                    }
                    if (selectedtypeoftimetbl == "Clinical") {
                        selectedtypeoftimetbl = "CL"
                    }

                    if (selectedtypeoftimetbl == "Select type") {

                        return
                    }
                    yearlist.clear()
                    semister.clear()
                    institute_name = spinner_institue.selectedItem.toString()
                    if (institute_name.equals("SPDC") || institute_name.equals("RNPC")) {
                        selectedtypeoftimetbl = "TH_CL"
                    }
                    println("second insti " + spinner_institue.selectedItem.toString())
                    println("selectedtypeoftimetbl  " + selectedtypeoftimetbl)
                    var phpApiInterface: PhpApiInterface =
                        ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                    var call: Call<TimeTableData> =
                        phpApiInterface.timetable_details(institute_name, selectedtypeoftimetbl)
                    call.enqueue(object : Callback<TimeTableData> {
                        override fun onFailure(call: Call<TimeTableData>, t: Throwable) {
                            Toast.makeText(
                                this@AcademicCalUploadInsti,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<TimeTableData>,
                            response: Response<TimeTableData>
                        ) {

                            var users = java.util.ArrayList<TimeTableDataC>()

                            if (response.isSuccessful) {
                                users.clear()
                                trsnsdlist = response.body()!!.Data
                                if (trsnsdlist!![0].ID == "error") {
                                    Toast.makeText(
                                        this@AcademicCalUploadInsti,
                                        "No Data in Time Table Master.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    users.clear()
                                    var listSize = trsnsdlist!!.size


                                    for (i in 0..listSize - 1) {

                                        if (trsnsdlist!![i].INSTITUTE == institute_name) {
                                            if (yearlist.contains(trsnsdlist!![i].YEAR)) {
                                                if (semister.contains(trsnsdlist!![i].SEMESTER)) {

                                                } else {
                                                    semister.add((trsnsdlist!![i].SEMESTER))
                                                }

                                            } else {

                                                yearlist.add(trsnsdlist!![i].YEAR)
                                                if (semister.contains(trsnsdlist!![i].SEMESTER)) {

                                                } else {
                                                    semister.add((trsnsdlist!![i].SEMESTER))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    yearlist.add("Select Year")

                    var yearAdap: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this@AcademicCalUploadInsti,
                            R.layout.support_simple_spinner_dropdown_item, yearlist
                        )
                    spinner_yearlist.adapter = yearAdap

                    semister.add("Select Semister")
                    var usersemlistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@AcademicCalUploadInsti,
                        R.layout.support_simple_spinner_dropdown_item,
                        semister
                    )
                    spinner_semester.adapter = usersemlistadp

                } catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@AcademicCalUploadInsti,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }
            else
            {
                GenericUserFunction.showInternetNegativePopUp(
                    this@AcademicCalUploadInsti,
                    getString(R.string.failureNoInternetErr))
            }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            type = "pdf"
            uri = data!!.data
            println("Uri pdf" + uri)
            println("pdfName pdf " + pdfName)
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

    private fun CheckValidation() {
        when {
            spinner_institue.selectedItem.toString().equals("Select Institute") -> Toast.makeText(
                this,
                "Please Select Institute",
                Toast.LENGTH_LONG
            ).show()
            spinner_timetabletype.selectedItem.toString().equals("Select type") -> Toast.makeText(
                this,
                "Please Select Type",
                Toast.LENGTH_LONG
            ).show()
            spinner_yearlist.selectedItem.toString().equals("Select Year") -> Toast.makeText(
                this,
                "Please Select Year",
                Toast.LENGTH_LONG
            ).show()
            spinner_semester.selectedItem.toString().equals("Select Semister") -> Toast.makeText(
                this,
                "Please Select Semister",
                Toast.LENGTH_LONG
            ).show()
            mediaPath == null -> println("no uri")
            else -> PdfUploadFunction()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun PdfUploadFunction() {
//        PdfPathHolder = FilePath.getPath(this, uri)
//        println("PdfNameHolder " + PdfNameHolder + "PdfPathHolder " + PdfPathHolder)
        subtype = spinner_timetabletype.selectedItem.toString()
        if (institute_name.equals("SPDC") || institute_name.equals("RNPC")) {
            subtype = "TH_CL"
        } else {
            if (subtype.equals("Theory")) {
                subtype = "TH"
            } else {
                subtype = "CL"
            }
        }
        year_tt = spinner_yearlist.selectedItem.toString()
        sem_tt = spinner_semester.selectedItem.toString()

        pdfName = institute_name + "_" + sem_tt + "_" + subtype + "_" + year_tt


        if (mediaPath == null) {

            Toast.makeText(
                this,
                "Please move your PDF file to internal storage & try again.",
                Toast.LENGTH_LONG
            ).show()

        } else {
            //Dialog Start
//            dialogCommon!!.setMessage("Please Wait!!! \nwhile we are Uploading Calender")
//            dialogCommon!!.setCancelable(false)
//            dialogCommon!!.show()

            uploadFile()

//            try {
//
//                //Dialog End
//
//
//                PdfID = UUID.randomUUID().toString()
//                uploadReceiver.setDelegate(this)
//                uploadReceiver.setUploadID(PdfID!!)
//
//                MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
//                    .addFileToUpload(PdfPathHolder, "pdf")
//                    .addParameter("name", pdfName)
//                    .addParameter("institute", institute_name)
//                    .addParameter("subtype", subtype)
//                    .addParameter("year", year_tt)
//                    .addParameter("sem", sem_tt)
//                    .setNotificationConfig(UploadNotificationConfig())
//                    .setMaxRetries(5)
//                    .startUpload()
//
//            } catch (exception: Exception) {
//                dialogCommon!!.dismiss()
//
//                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//            }

        }
    }

    //
    companion object {

        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/uploadAcademicCalender.php"
    }

    private fun uploadFile() {
        if (InternetConnection.checkConnection(this)) {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Time Table.")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)

        // Parsing any Media type file
        var longString=pdfName+"@cut"+institute_name+"@cut"+subtype+"@cut"+year_tt+"@cut"+sem_tt
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file",longString, requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), longString)

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =
            phpApiInterface.uploadInsTimeTable(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                val serverResponse = response.body()
                if (serverResponse != null) {
                    dialogCommon!!.dismiss()
                    if (serverResponse.success) {

                        GenericUserFunction.showPositivePopUp(this@AcademicCalUploadInsti,serverResponse.message.toString())


                    } else {
                        GenericUserFunction.showNegativePopUp(this@AcademicCalUploadInsti,serverResponse.message.toString())

                    }
                } else {
                    dialogCommon!!.dismiss()
//                    assert(serverResponse != null)
//                    Log.v("Response", serverResponse!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                dialogCommon!!.dismiss()
                GenericUserFunction.showNegativePopUp(this@AcademicCalUploadInsti,t.message.toString())

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
