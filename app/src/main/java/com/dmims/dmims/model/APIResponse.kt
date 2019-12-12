package com.dmims.dmims.model

class APIResponse {
     var Responsecode: Int = 0
     var Data: GetMobileNo? = null
     var Data1: GetStudentData? = null
     var Data12: List<GetGreivance>? = null
     var Data15: GetAppVersion? = null
     var Data16: StudentSearchRoll? = null
     var Data17:List<GrievanceStudGetData>?=null
     var Data18: List<GrievanceDataField>? = null
     var Data19: UserDataField? = null
     var Data20: Data20? = null
     var Data14: List<GetNotice>? = null
     var Data2: GetParentData? = null
     var Data3: GetFacultyData? = null
     var Data4: GetAdminData? = null
     var Data5: List<GetAttendance>? = null
     var Data13: List<GetProgressiveAttend>? = null
     var Data6: List<InstituteData>? = null
     var Data9: List<GetEmergencyContact>? = null
     var Data7: StudentSearch? = null
     var success: String = ""
     var Status: String = ""
     var Status1: String = ""
     var response: String = ""
}