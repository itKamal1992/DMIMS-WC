package com.dmims.dmims.dataclass

data class StudentGrievanceGet(val  G_ID         : String,
                               val  STUD_ID      : String,
                               val  course_id    : String,
                               val  roll_no      : String,
                               val  Grev_name    : String,
                               val  Inst_Name    : String,
                               val  Comp_To      : String,
                               val  Grev_Filename: String,
                               val  DEPARTMENT   : String,
                               val  ATTACHMENT_URL: String,
                               val  G_SUBJECT    : String,
                               val  G_CATEGORY   : String,
                               val  G_AGAINST    : String,
                               val  G_DISCRIPTION: String,
                               var  G_DATE       : String,
                               var  G_ATTACHMENT : String,
                               var  G_STATUS     : String,
                               var  ASSING_TO_ID  : String,
                               var  REMINDER      : String,
                               var  G_COMMENT     : String ?=null,
                               var  TO_DATE       : String ?=null,
                               var  FROM_DATE     : String ?=null,
                               val image: Int,
                               var  down_panal     : Boolean

)