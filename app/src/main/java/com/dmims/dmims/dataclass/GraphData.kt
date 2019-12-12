package com.dmims.dmims.dataclass

data class GraphData(
    val graph_desc: String,
    val No_of_bar: Int,
    var BarDataList: List<BarDatas>? = null

)

