package com.dmims.dmims.ExamFeedBack


class CommonFeedBack {
    private var FEEDBACK_TYPE: String
    private var COURSE_ID: String
    private var STUD_ID: String
    private var STUD_NAME: String
    private var ROLL_NO: String
    private var COURSE: String
    private var INSTITUTE_NAME: String
    private var Formative: Formative?=null
    private var Summative:Summative ?= null

    constructor(FEEDBACK_TYPE: String, COURSE_ID: String, STUD_ID: String, STUD_NAME: String, ROLL_NO: String, COURSE: String, INSTITUTE_NAME: String, Formative: Formative
    ) {
        this.FEEDBACK_TYPE = FEEDBACK_TYPE
        this.COURSE_ID = COURSE_ID
        this.STUD_ID = STUD_ID
        this.STUD_NAME = STUD_NAME
        this.ROLL_NO = ROLL_NO
        this.COURSE = COURSE
        this.INSTITUTE_NAME = INSTITUTE_NAME
        this.Formative = Formative
    }

    constructor(
        FEEDBACK_TYPE: String, COURSE_ID: String, STUD_ID: String, STUD_NAME: String, ROLL_NO: String, COURSE: String, INSTITUTE_NAME: String, Summative: Summative
    ) {
        this.FEEDBACK_TYPE = FEEDBACK_TYPE
        this.COURSE_ID = COURSE_ID
        this.STUD_ID = STUD_ID
        this.STUD_NAME = STUD_NAME
        this.ROLL_NO = ROLL_NO
        this.COURSE = COURSE
        this.INSTITUTE_NAME = INSTITUTE_NAME
        this.Summative = Summative
    }


//    init {
//        this.FEEDBACK_TYPE = FEEDBACK_TYPE
//        this.COURSE_ID = COURSE_ID
//        this.STUD_ID = STUD_ID
//        this.STUD_NAME = STUD_NAME
//        this.ROLL_NO = ROLL_NO
//        this.COURSE = COURSE
//        this.INSTITUTE_NAME = INSTITUTE_NAME
//        this.formativeSub = formativeSub
//    }

}

class Formative(
    FACULTY_TYPE: String,
    FEED_SUM_DATE: String,
    FOR_DESC: String,
    Feed_Form_SectA: Feed_Form_SectA,
    Feed_Form_SectB: Feed_Form_SectB
) {
    private var FACULTY_TYPE: String
    private var FEED_SUM_DATE: String
    private var FOR_DESC: String
    private var Feed_Form_SectA: Feed_Form_SectA
    private var Feed_Form_SectB: Feed_Form_SectB

    init {
        this.FACULTY_TYPE = FACULTY_TYPE
        this.FEED_SUM_DATE = FEED_SUM_DATE
        this.FOR_DESC = FOR_DESC
        this.Feed_Form_SectA = Feed_Form_SectA
        this.Feed_Form_SectB = Feed_Form_SectB
    }
}

class Feed_Form_SectA(
    FA1: String,
    FA2: String,
    FA3: String,
    FA4: String,
    FA5: String
) {
    private var FA1: String
    private var FA2: String
    private var FA3: String
    private var FA4: String
    private var FA5: String

    init {
        this.FA1 = FA1
        this.FA2 = FA2
        this.FA3 = FA3
        this.FA4 = FA4
        this.FA5 = FA5
    }
}

class Feed_Form_SectB(
    FB1: String,
    FB2: String,
    FB3: String,
    FB3_DES: String,
    FB4: String,
    FB5A: String,
    FB5A_DES: String,
    FB5B: String,
    FB5B_DES: String,
    FB5C: String,
    FB5C_DES: String,
    FB5D: String,
    FB5D_DES: String
) {
    private var FB1: String
    private var FB2: String
    private var FB3: String
    private var FB3_DES: String
    private var FB4: String
    private var FB5A: String
    private var FB5A_DES: String
    private var FB5B: String
    private var FB5B_DES: String
    private var FB5C: String
    private var FB5C_DES: String
    private var FB5D: String
    private var FB5D_DES: String

    init {
        this.FB1 = FB1
        this.FB2 = FB2
        this.FB3 = FB3
        this.FB3_DES = FB3_DES
        this.FB4 = FB4
        this.FB5A = FB5A
        this.FB5A_DES = FB5A_DES
        this.FB5B = FB5B
        this.FB5B_DES = FB5B_DES
        this.FB5C = FB5C
        this.FB5C_DES = FB5C_DES
        this.FB5D = FB5D
        this.FB5D_DES = FB5D_DES
    }

}

class Summative(


    FACULTY_TYPE: String,
    EXAM_TYPE: String,
    EXAM_YEAR: String,
    FEED_SUM_DATE: String,
    SUM_DESC: String,
    Feed_Sum_SectA:Feed_Sum_SectA,
    Feed_Sum_SectB:Feed_Sum_SectB,
    Feed_Sum_SectC:Feed_Sum_SectC,
    Feed_Sum_SectD:Feed_Sum_SectD

) {
    private var FACULTY_TYPE: String
    private var EXAM_TYPE: String
    private var EXAM_YEAR: String
    private var FEED_SUM_DATE: String
    private var SUM_DESC: String
    private var Feed_Sum_SectA:Feed_Sum_SectA
    private var Feed_Sum_SectB:Feed_Sum_SectB
    private var Feed_Sum_SectC:Feed_Sum_SectC
    private var Feed_Sum_SectD:Feed_Sum_SectD

    init {
        this.FACULTY_TYPE = FACULTY_TYPE
        this.EXAM_TYPE = EXAM_TYPE
        this.EXAM_YEAR = EXAM_YEAR
        this.FEED_SUM_DATE = FEED_SUM_DATE
        this.SUM_DESC = SUM_DESC
        this.Feed_Sum_SectA=Feed_Sum_SectA
        this.Feed_Sum_SectB=Feed_Sum_SectB
        this.Feed_Sum_SectC=Feed_Sum_SectC
        this.Feed_Sum_SectD=Feed_Sum_SectD

    }

}

class Feed_Sum_SectA(
    SA1: String,
    SA1_DES: String,
    SA2: String,
    SA2_DES: String,
    SA3: String,
    SA3_DES: String,
    SA4: String,
    SA4_DES: String

) {
    private var SA1: String
    private var SA1_DES: String
    private var SA2: String
    private var SA2_DES: String
    private var SA3: String
    private var SA3_DES: String
    private var SA4: String
    private var SA4_DES: String

    init {
        this.SA1 = SA1
        this.SA1_DES = SA1_DES
        this.SA2 = SA2
        this.SA2_DES = SA2_DES
        this.SA3 = SA3
        this.SA3_DES = SA3_DES
        this.SA4 = SA4
        this.SA4_DES = SA4_DES
    }
}

class Feed_Sum_SectB(
    SB1: String,
    SB1_DES: String,
    SB2: String,
    SB2_DES: String,
    SB3: String,
    SB3_DES: String,
    SB4: String,
    SB4_DES: String,
    SB5: String,
    SB5_DES: String
) {
    private var SB1 :String
    private var SB1_DES :String
    private var SB2 :String
    private var SB2_DES :String
    private var SB3 :String
    private var SB3_DES :String
    private var SB4 :String
    private var SB4_DES :String
    private var SB5 :String
    private var SB5_DES :String

    init {
        this.SB1 = SB1
        this.SB1_DES = SB1_DES
        this.SB2 = SB2
        this.SB2_DES = SB2_DES
        this.SB3 = SB3
        this.SB3_DES = SB3_DES
        this.SB4 = SB4
        this.SB4_DES = SB4_DES
        this.SB5 = SB5
        this.SB5_DES = SB5_DES
    }
}
class Feed_Sum_SectC(
    SC1:String,
    SC1_DES:String,
    SC2:String,
    SC2_DES:String,
    SC3:String,
    SC3_DES:String,
    SC4:String,
    SC4_DES:String
){
    private var SC1:String
    private var SC1_DES:String
    private var SC2:String
    private var SC2_DES:String
    private var SC3:String
    private var SC3_DES:String
    private var SC4:String
    private var SC4_DES:String
    init {
        this.SC1=SC1
        this.SC1_DES=SC1_DES
        this.SC2=SC2
        this.SC2_DES=SC2_DES
        this.SC3=SC3
        this.SC3_DES=SC3_DES
        this.SC4=SC4
        this.SC4_DES=SC4_DES
    }
}

class Feed_Sum_SectD(
    SD1:String,
    SD1_DES:String,
    SD2:String,
    SD2_DES:String,
    SD3:String,
    SD3_DES:String,
    SD4:String,
    SD4_DES:String,
    SD5:String,
    SD5_DES:String,
    SD6:String,
    SD6_DES:String
)
{
    private var SD1:String
    private var SD1_DES:String
    private var SD2:String
    private var SD2_DES:String
    private var SD3:String
    private var SD3_DES:String
    private var SD4:String
    private var SD4_DES:String
    private var SD5:String
    private var SD5_DES:String
    private var SD6:String
    private var SD6_DES:String

    init {
        this.SD1=SD1
        this.SD1_DES=SD1_DES
        this.SD2=SD2
        this.SD2_DES=SD2_DES
        this.SD3=SD3
        this.SD3_DES=SD3_DES
        this.SD4=SD4
        this.SD4_DES=SD4_DES
        this.SD5=SD5
        this.SD5_DES=SD5_DES
        this.SD6=SD6
        this.SD6_DES=SD6_DES
    }

}

