package com.d3if3150.catatin.utils.network

import com.d3if3150.catatin.model.RealEstate
import com.d3if3150.catatin.utils.network.ApiFetchUtil.localApiFetchList

object ApiFetchUtil {
    var localApiFetchList: Array<RealEstate> = arrayOf()
}

fun getApiFetchList(): Array<RealEstate> {
    return localApiFetchList
}