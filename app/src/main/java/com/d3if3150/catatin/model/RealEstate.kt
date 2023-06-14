package com.d3if3150.catatin.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class RealEstate(
    val id: String,
    val img_src: String,
    val price: Int,
    val type: String
): Parcelable