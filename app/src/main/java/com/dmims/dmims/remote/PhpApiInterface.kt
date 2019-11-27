package com.dmims.dmims.remote

import com.dmims.dmims.ImageClass
import com.dmims.dmims.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.PartMap
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Multipart



interface PhpApiInterface  {




    @FormUrlEncoded
    @POST("imageupload/upload.php")
    fun uploadImage(@Field("title") title: String,@Field("image") image: String): Call<ImageClass>

    @FormUrlEncoded
    @POST("time_table/view_time_table_details.php")
    fun timetable_details(@Field("institute_name") institute_name: String,@Field("selectedtypeoftimetbl") selectedtypeoftimetbl: String): Call<TimeTableData>

    @FormUrlEncoded
    @POST("time_table/view_time_table_url.php")
    fun timetable_details_url(@Field("institute_name") institute_name: String,@Field("selectedtypeoftimetbl") selectedtypeoftimetbl: String,@Field("selectedyear") selectedyear: String,@Field("selectedsemtype") selectedsemtype: String): Call<TimeTableData>

    @POST("feedback_type/feedback_master.php")
    fun feedback_master(): Call<FeedBackMaster>

    @FormUrlEncoded
    @POST("feedback_type/feedbackScheInsrt.php")
    fun InsertFeedBackScheduler(@Field("INSTITUTE_NAME") INSTITUTE_NAME: String,@Field("COURSE_NAME") COURSE_NAME: String, @Field("DEPT_NAME") DEPT_NAME: String, @Field("FEEDBACK_NAME") FEEDBACK_NAME: String, @Field("SCHEDULE_DATE") SCHEDULE_DATE: String, @Field("START_DATE") START_DATE: String, @Field("END_DATE") END_DATE: String,@Field("YEAR")Year:String): Call<FeedBackInsert>


  @FormUrlEncoded
    @POST("feedback_type/UpdatefeedbackScheInsrt.php")
    fun UpdateFeedBackScheduler(@Field("INSTITUTE_NAME") INSTITUTE_NAME: String,@Field("COURSE_NAME") COURSE_NAME: String, @Field("DEPT_NAME") DEPT_NAME: String, @Field("FEEDBACK_NAME") FEEDBACK_NAME: String, @Field("SCHEDULE_DATE") SCHEDULE_DATE: String, @Field("START_DATE") START_DATE: String, @Field("END_DATE") END_DATE: String,@Field("YEAR")Year:String,@Field("ID")ID:String): Call<FeedBackInsert>

    @FormUrlEncoded
    @POST("feedback_type/feedbackDeptList.php")
    fun DepartDetailsStudYear(@Field("COURSE_ID") COURSE_ID: String): Call<DeptListStudData>

    @FormUrlEncoded
    @POST("feedback_type/feedbackiInstList.php")
    fun InstDetailsStudYear(@Field("COURSE_ID") COURSE_ID: String): Call<DeptListStudData>

    //REST API CALL
    @POST("dmims/api/apiversion/read.php")
    fun api_version(): Call<ApiVersion>

    @FormUrlEncoded
    @POST("dmims/api/course_master_test/read_single.php")
    fun InstDetails(@Field("COURSE_ID") COURSE_ID: String): Call<DeptListStudDataRef>
    //END REST API
    @FormUrlEncoded
    @POST("feedback_type/GetOriginalDept.php")
    fun GetOriginalDept(@Field("INSTITUTE_NAME") INSTITUTE_NAME: String): Call<DeptListStudData>

    @FormUrlEncoded
    @POST("feedback_type/feedbackFacutList.php")
    fun FacultDetailsStudYear(@Field("COURSE_ID") COURSE_ID: String,@Field("selectedDept") selectedDept: String): Call<DeptListStudData>

    @FormUrlEncoded
    @POST("feedback_type/feedbackTeacherInsrt.php")
    fun feedbackTeacherInsrt(@Field("STUDENT_ID") STUDENT_ID: String,@Field("STUDENT_NAME") STUDENT_NAME: String, @Field("STUD_INST_NAME") STUD_INST_NAME: String,
                             @Field("FEEDBACK_NAME") FEEDBACK_NAME: String, @Field("FEEDBACK_SEC_ID") FEEDBACK_SEC_ID: String, @Field("FEEDBACK_SEC_ID_NAME") FEEDBACK_SEC_ID_NAME: String, @Field("DEPT_NAME") DEPT_NAME: String, @Field("TEACHER_NAME") TEACHER_NAME: String, @Field("TEACHER_STATUS") TEACHER_STATUS: String,
                             @Field("QID1") QID1: String, @Field("QID2") QID2: String, @Field("QID3") QID3: String, @Field("QID4") QID4: String,
                             @Field("QID5") QID5: String, @Field("QID6") QID6: String, @Field("QID7") QID7: String, @Field("QID8") QID8: String,
                             @Field("QID9") QID9: String, @Field("QID10") QID10: String, @Field("QID11") QID11: String, @Field("QID12") QID12: String,
                             @Field("QID13") QID13: String, @Field("QID14") QID14: String, @Field("QID15") QID15: String, @Field("QID16") QID16: String,
                             @Field("QID17") QID17: String, @Field("QID18") QID18: String, @Field("QID19") QID19: String, @Field("QID20") QID20: String,
                             @Field("QID21") QID21: String,
                             @Field("QANS1") QANS1: String, @Field("QANS2") QANS2: String, @Field("QANS3") QANS3: String,
                             @Field("QANS4") QANS4: String,@Field("QANS5") QANS5: String, @Field("QANS6") QANS6: String, @Field("QANS7") QANS7: String, @Field("QANS8") QANS8: String,
                             @Field("QANS9") QANS9: String, @Field("QANS10") QANS10: String, @Field("QANS11") QANS11: String, @Field("QANS12") QANS12: String,
                             @Field("QANS13") QANS13: String, @Field("QANS14") QANS14: String, @Field("QANS15") QANS15: String, @Field("QANS16") QANS16: String,
                             @Field("QANS17") QANS17: String, @Field("QANS18") QANS18: String, @Field("QANS19") QANS19: String, @Field("QANS20") QANS20: String,
                             @Field("QANS21") QANS21: String,
                             @Field("SUGGEST") SUGGEST: String,
                             @Field("FEEDBACK_DATE") FEEDBACK_DATE: String): Call<FeedBackInsert>

    @FormUrlEncoded
    @POST("newRegister/newRegister.php")
    fun NewRegisteration(
        @Field("Name") Name: String,
        @Field("Email") Email: String,
        @Field("MobNo") MobNo: String,
        @Field("RollNo") RollNo: String,
        @Field("EnrolNo") EnrolNo: String,
        @Field("InstituteName") InstituteName: String,
        @Field("Course") Course: String,
        @Field("Password") Password: String,
        @Field("student_id") student_id: String,
        @Field("course_id") course_id: String,
        @Field("sem_id") sem_id: String,
        @Field("Date") Date: String

    ): Call<NewUserInsert>

    @FormUrlEncoded
    @POST("newRegister/newRegisterNot.php")
    fun NewRegisterationC(
        @Field("Name") Name: String,
        @Field("Email") Email: String,
        @Field("MobNo") MobNo: String,
        @Field("RollNo") RollNo: String,
        @Field("EnrolNo") EnrolNo: String,
        @Field("InstituteName") InstituteName: String,
        @Field("Course") Course: String,
        @Field("student_id") student_id: String,
        @Field("course_id") course_id: String,
        @Field("sem_id") sem_id: String,
        @Field("Date") Date: String

    ): Call<NewUserInsert>

    @FormUrlEncoded
    @POST("newRegister/VerifyUserDetails.php")
    fun VerifyUserDetails(
        @Field("MobNo") MobNo: String
    ): Call<NewUserInsert>

    @FormUrlEncoded
    @POST("newRegister/VerifyOtpMob.php")
    fun VerifyOtpMob(
        @Field("MobNo") MobNo: String
    ,@Field("PASSWORD") PASSWORD: String): Call<NewUserInsert>

    @FormUrlEncoded
    @POST("newRegister/DUMMYFTMAIN.php")
    fun DUMMYFTMAIN(
        @Field("STUDENT_ID") STUDENT_ID: String
        ,@Field("FEEDBACK_SEC_ID") FEEDBACK_SEC_ID: String): Call<NewUserInsert>

    @FormUrlEncoded
    @POST("newRegister/DUMMYFT.php")
    fun DUMMYFT(
        @Field("STUDENT_ID") STUDENT_ID: String
        ,@Field("FEEDBACK_SEC_ID") FEEDBACK_SEC_ID: String): Call<NewUserInsert>


    @FormUrlEncoded
    @POST("feedback_type/IGetData.php")
    fun IGetData(@Field("COURSE_INSTITUTE") COURSE_INSTITUTE: String,@Field("COURSE_NAME") COURSE_NAME: String): Call<DeptListStudData>


    @FormUrlEncoded
    @POST("feedback_type/StatusSubmited.php")
    fun StatusSubmited(@Field("STUDENT_ID") STUDENT_ID: String): Call<DeptListStudData>

    @Multipart
    @POST("Api.php?apicall=upload")
    fun uploadImage2(@Part("image\"; filename=\"myfile.jpg\" ") file: RequestBody, @Part("desc") desc: RequestBody): Call<MyResponse>


    @GET("pdfupload/GetMCQUploadData.php")
    fun GetUploadMCQ(): Call<MCQListUpload>

    @FormUrlEncoded
    @POST("pdfupload/GetMCQUploadByDate.php")
    fun GetUploadMCQbyDate(@Field("CURRENT_DATE") CurrentDate: String): Call<MCQListUpload>

    @FormUrlEncoded
    @POST("feedback_type/GetFeedbackDetails.php")
    fun CurrentDateSubmit(@Field("CURRENT_DATE") CurrentDate: String): Call<FeedBackSchedule>

    @FormUrlEncoded
    @POST("pdfupload/PostDeleteMCQ.php")
    fun deleteMCQ(@Field("id") id: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("pdfupload/read.php")
    fun readpdfpath(@Field("id") id: String): Call<ApiVersion>

    @Multipart
    @POST("pdfupload/upload_image.php")
    abstract fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): Call<ServerResponse>

    @Multipart
    @POST("pdfupload/uploadmcqkey.php")
    abstract fun uploadMcqPdf(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): Call<ServerResponse>

    @FormUrlEncoded
    @POST("pdfupload/insert_mcq_info.php")
    fun InsertMcqKeyInfo(@Field("Institute") Institute: String,
                         @Field("UserName") UserName: String,
                         @Field("UserMobileNO") UserMobileNO: String,
                         @Field("UserRole") UserRole: String,
                         @Field("UserEmail") UserEmail: String,
                         @Field("UserDesig") UserDesig: String,
                         @Field("Course") Course: String,
                         @Field("Department") Department: String,
                         @Field("Year") Year: String,
                         @Field("McqUploadDate") McqUploadDate: String,
                         @Field("StartDate") StartDate: String,
                         @Field("EndDate") EndDate: String,
                         @Field("FileUrl") FileUrl: String,
                         @Field("FileName") FileName: String): Call<ServerResponse>


    @Multipart
    @POST("imageupload/upload_grievancepdf.php")
    abstract fun uploadGrievancePdf(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): Call<ServerResponse>


    @Multipart
    @POST("upload_acd_cal_u.php")
    abstract fun uploadAcdCal(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody

    ): Call<ServerResponse>


    @FormUrlEncoded
    @POST("ViewAcademicCalender.php")
    fun getAcdTimeTable(@Field("Session") Session: String): Call<ServerResponse>

    @Multipart
    @POST("TimeTableNewUpload.php")
    abstract fun uploadInsTimeTable(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody

    ): Call<ServerResponse>


    @Multipart
    @POST("imageupload/upload_greivanceimage.php")
    abstract fun uploadGrievanceImg(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): Call<ServerResponse>

    @Multipart
    @POST("pdfupload/notice_common_upload.php")
    abstract fun noticeCommonUpload(
        @Part file: MultipartBody.Part,
        @Part("file") name: RequestBody
    ): Call<ServerResponse>


}

