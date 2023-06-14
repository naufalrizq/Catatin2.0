package com.d3if3150.catatin.repo

import com.d3if3150.catatin.model.RealEstate
import com.d3if3150.catatin.remote.Api
import retrofit2.Response
import javax.inject.Inject

class RealEstateRepository @Inject constructor(
    private val api: Api
) {

    //Get responses for calls made by Api
    suspend fun getRealEstateItems(): Response<List<RealEstate>> {
        return api.getRealEstateItems()
    }
}