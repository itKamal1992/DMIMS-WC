package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeptListStudDataRef {

    @SerializedName("DEPT_ID")
    @Expose
    var DEPT_ID: String = ""

    @SerializedName("DEPT_NAME")
    @Expose
    var DEPT_NAME: String = ""

    @SerializedName("COURSE_ID")
    @Expose
    var COURSE_ID: String = ""

    @SerializedName("ID")
    @Expose
    var ID: String = ""

    @SerializedName("FACULTY_ID")
    @Expose
    var FACULTY_ID: String = ""

    @SerializedName("FNAME")
    @Expose
    var FNAME: String = ""

    @SerializedName("MNAME")
    @Expose
    var MNAME: String = ""

    @SerializedName("LNAME")
    @Expose
    var LNAME: String = ""

    @SerializedName("MOBILE_NO")
    @Expose
    var MOBILE_NO: String = ""

    @SerializedName("COURSE_NAME")
    @Expose
    var COURSE_NAME: String = ""

    @SerializedName("SPECIALISATION")
    @Expose
    var SPECIALISATION: String = ""

    @SerializedName("COURSE_INSTITUTE")
    @Expose
    var COURSE_INSTITUTE: String = ""

    @SerializedName("INSTITUTE_NAME")
    @Expose
    var INSTITUTE_NAME: String = ""

    @SerializedName("DEPARTMENT_NAME")
    @Expose
    var DEPARTMENT_NAME: String = ""

    @SerializedName("YEAR")
    @Expose
    var YEAR: String = ""

    @SerializedName("STUDENT_ID")
    @Expose
    var STUDENT_ID: String = ""

    @SerializedName("STUD_INST_NAME")
    @Expose
    var STUD_INST_NAME: String = ""

    @SerializedName("FEEDBACK_NAME")
    @Expose
    var FEEDBACK_NAME: String = ""


    @SerializedName("FEEDBACK_SEC_ID_NAME")
    @Expose
    var FEEDBACK_SEC_ID_NAME: String = ""

    @SerializedName("FEEDBACK_SEC_ID")
    @Expose
    var FEEDBACK_SEC_ID: String = ""

    @SerializedName("TEACHER_STATUS")
    @Expose
    var TEACHER_STATUS: String = ""

    @SerializedName("TEACHER_NAME")
    @Expose
    var TEACHER_NAME: String = ""
}