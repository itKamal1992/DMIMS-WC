package com.dmims.dmims.remote

import com.dmims.dmims.ExamFeedBack.CommonFeedBack
import com.dmims.dmims.activity.Task
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.GetGreivance
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface IMyAPI {

    @FormUrlEncoded
    @POST("Login/GetOtp")
    fun GetOtp(@Field("MobileNo") mobileno: String): Call<APIResponse>

    @GET("Feedback/GetGrievanceReport")
    fun GetRegisteredGreivance(): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/StudentSearch")
    fun StudentSearch(@Field("student_id") STUDENTID: Int): Call<APIResponse>

    @FormUrlEncoded
    @POST("Attendance/GetAttendance")
    fun GetAttendance(@Field("STUDENTID") STUDENTID: Int, @Field("FROMDATE") FROMDATE: String, @Field("TODATE") TODATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Attendance/GetProgressiveAttend")
    fun GetProgressiveAttend(@Field("STUDENTID") STUDENTID: Int, @Field("FROMDATE") FROMDATE: String, @Field("TODATE") TODATE: String, @Field("COURSE_ID")COURSE_ID:String): Call<APIResponse>


    @FormUrlEncoded
    @POST("Login/VerifyOtp")
    fun VerifyOtp(@Field("MobileNo") mobileno: String, @Field("Otp") password: String): Call<APIResponse>


    @GET("Login/GetInstituteData")
    fun GetInstituteData(): Call<APIResponse>

    @GET("EmergencyServices/GetEmergencyServices")
    fun GetEmergencyServices(): Call<APIResponse>

    @GET("AppVersion/AppVersion")
    fun AppVersion(): Call<APIResponse>

//    @Multipart
//    @POST("Upload/PostFormData")
//    //fun PostFormData(@Part file: MultipartBody.Part): Call<APIResponse>
//    fun PostFormData(@Part file: MultipartBody.Part,@Part("NOTICE_DATE")notice_date:String,@Part("NOTICE_TITLE")notice_title:String,@Part("NOTICE_DESC")notice_desc:String,@Part("INSTITUE_NAME")selectedInstituteName:String,@Part("COURSE_NAME")selectedcourselist:String,@Part("DEPT_NAME")selecteddeptlist:String,@Part("NOTICE_TYPE")selectedNoticeType:String,@Part("USER_TYPE")selectedFacultyStud:String,@Part("RESOU_FLAG")RESOU_FLAG:String,@Part("USER_ROLE")roleadmin:String,@Part("USER_ID")id_admin:String): Call<APIResponse>
//old method
    @FormUrlEncoded
    @POST("Upload/UploadNotice")
    fun UploadNotice(
        @Field("NOTICE_DATE") notice_date: String, @Field("NOTICE_TITLE") notice_title: String, @Field("NOTICE_DESC") notice_desc: String, @Field(
            "INSTITUTE_NAME"
        ) selectedInstituteName: String, @Field("COURSE_NAME") selectedcourselist: String, @Field("DEPT_NAME") selecteddeptlist: String, @Field(
            "NOTICE_TYPE"
        ) selectedNoticeType: String, @Field("USER_TYPE") selectedFacultyStud: String, @Field("RESOU_FLAG") RESOU_FLAG: String, @Field(
            "USER_ROLE"
        ) roleadmin: String, @Field("USER_ID") id_admin: String, @Field("FILENAME") filename: String, @Field("COURSE_ID") course_id: String, @Field(
            "DEPT_ID"
        ) dept_id: String, @Field("STUDENT_FLAG") student_flag: String, @Field("FACULTY_FLAG") faculty_flag: String, @Field(
            "ADMIN_FLAG"
        ) admin_flag: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("Upload/UploadNotice")
    fun UploadNotice(
        @Field("NOTICE_DATE") notice_date: String, @Field("NOTICE_TITLE") notice_title: String, @Field("NOTICE_DESC") notice_desc: String, @Field(
            "INSTITUTE_NAME"
        ) selectedInstituteName: String, @Field("COURSE_NAME") selectedcourselist: String, @Field("DEPT_NAME") selecteddeptlist: String, @Field(
            "NOTICE_TYPE"
        ) selectedNoticeType: String, @Field("USER_TYPE") selectedFacultyStud: String, @Field("RESOU_FLAG") RESOU_FLAG: String, @Field(
            "USER_ROLE"
        ) roleadmin: String, @Field("USER_ID") id_admin: String, @Field("FILENAME") filename: String, @Field("COURSE_ID") course_id: String, @Field(
            "DEPT_ID"
        ) dept_id: String, @Field("STUDENT_FLAG") student_flag: String, @Field("FACULTY_FLAG") faculty_flag: String, @Field(
            "ADMIN_FLAG"
        ) admin_flag: String, @Field(
            "YEAR"
        ) YEAR: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/InsertGrievanceReport")
    fun InsertGrievanceReport(
        @Field("INFORMATION") sel_information: String, @Field("NAME_GRIE") sel_name: String, @Field("PHONE_NO") sel_mobileno: String, @Field(
            "EMAIL_ID"
        ) sel_emailid: String, @Field("ADDRESS") sel_address: String, @Field("PLACE_WORK") sel_workplace: String, @Field(
            "JOB_TITLE"
        ) sel_jobtitle: String, @Field("TYPE_GRIE") selectedgreviancetype: String, @Field("DT_TIME_PLACE") sel_datetime: String, @Field(
            "DETAIL_DESC"
        ) sel_detaildesc: String, @Field("OTHER_INFO") sel_proposedsol: String, @Field(
            "PRO_SOL_GRIE"
        ) course_id: String, @Field(
            "OTHER_DETAILS"
        ) sel_providername: String,
        @Field("STUDENT_ID") stud_k: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/GetInsertNotice")
    fun GetNotice(@Field("FROM_NOTICE_DATE") FROM_NOTICE_DATE: String, @Field("TO_NOTICE_DATE") TO_NOTICE_DATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/DeleteNotice")
    fun DeleteNotice(@Field("ID") ID: Int): Call<APIResponse>



    @GET("Feedback/GetFeedBackScheduler")
    fun GetFeedBackScheduler(): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/InsertFeedBack")
    fun InsertFeedBack(@Field("COURSE_NAME") COURSE_NAME: String, @Field("DEPT_NAME") DEPT_NAME: String, @Field("FEEDBACK_NAME") FEEDBACK_NAME: String, @Field("SCHEDULE_DATE") SCHEDULE_DATE: String, @Field("START_DATE") START_DATE: String, @Field("END_DATE") END_DATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/StudentSearchByRollNo")
    fun StudentSearchByRollNo(@Field("roll_no") roll_no: String, @Field("course_id")course_id:String): Call<APIResponse>


    @POST("Feedback/Feedback_Form_Summ")
//    fun SubmitExamFeedback(@Body task: Task): Call<APIResponse>
    fun SubmitExamFeedback(@Body commonFeedBack: CommonFeedBack): Call<APIResponse>

//    fun SubmitExamFeedback(@Field ("FEEDBACK_TYPE")FEEDBACK_TYPE: String,
//                           @Field ("COURSE_ID")COURSE_ID: String,
//                           @Field ("STUD_ID")STUD_ID: String,
//                           @Field ("STUD_NAME")STUD_NAME: String,
//                           @Field ("ROLL_NO")ROLL_NO: String,
//                           @Field ("COURSE")COURSE: String,
//                           @Field ("INSTITUTE_NAME")INSTITUTE_NAME: String,
//                           @Field ("Formative1")Formative1: String)
//            : Call<APIResponse>




    @FormUrlEncoded
    @POST("feedback/OnlineGrievanceReport")
    fun InsertStudentGrievance( @Field("G_SUBJECT")G_SUBJECT: String,
                                @Field("G_CATEGORY")G_CATEGORY: String,
                                @Field("G_AGAINST")G_AGAINST: String,
                                @Field("G_DISCRIPTION")G_DISCRIPTION: String,
                                @Field("G_DATE")G_DATE: String,
                                @Field("G_ATTACHMENT")G_ATTACHMENT: String,
                                @Field("G_STATUS")G_STATUS: String,
                                @Field("ASSING_TO_ID")ASSING_TO_ID: Int,
                                @Field("REMINDER")REMINDER: String,
                                @Field("STUD_ID")STUD_ID: Int,
                                @Field("course_id")course_id: String,
                                @Field("roll_no")roll_no: String,
                                @Field("Grev_name")Grev_name: String,
                                @Field("Inst_Name")Inst_Name: String,
                                @Field("Comp_To")Comp_To: String,
                                @Field("Grev_Filename")Grev_Filename: String,
                                @Field("DEPARTMENT")DEPARTMENT: String,
                                @Field("ATTACHMENT_URL")ATTACHMENT_URL: String,
                                @Field("G_COMMENT")G_COMMENT: String

    ):Call<APIResponse>


    @FormUrlEncoded
    @POST("Feedback/Get_Clg_Head")
    fun GetGreivanceData(@Field("INST_NAME") INST_NAME: String, @Field("ROLL")ROLL:String): Call<APIResponse>


    @GET("Feedback/GetOnlineGrievanceReport")
    fun GetStudGrievanceSubmited( ):Call<APIResponse>


    @FormUrlEncoded
    @POST("Feedback/GetDatewiseOnlineGrievanceReport")
    fun GetDatewiseOnlineGrievanceReport(@Field("FROM_DATE") FROM_DATE: String, @Field("TO_DATE") TO_DATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/UpdateOnlineGrievanceReport")
    fun GreivanceUpdate(@Field("G_ID") G_ID: String,
                        @Field("Comp_TO") Comp_TO: String,
                        @Field("DEPARTMENT") DEPARTMENT: String,
                        @Field("G_COMMENT") G_COMMENT: String,
                        @Field("G_STATUS") G_STATUS: String,
                        @Field("ASSING_TO_ID") ASSING_TO_ID: Int,
                        @Field("REMINDER") REMINDER: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/GetPrincipalDeanDataDetails")
    fun CheckPrincipal(@Field("ADMIN_MOBILE_NO") ADMIN_MOBILE_NO: String): Call<APIResponse>
}

