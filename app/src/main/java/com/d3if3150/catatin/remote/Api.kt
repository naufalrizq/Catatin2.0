package com.d3if3150.catatin.remote

import com.d3if3150.catatin.model.RealEstate
import retrofit2.Response
import retrofit2.http.GET


interface Api {

    @GET("realestate")
    suspend fun getRealEstateItems(
    ): Response<List<RealEstate>>
}