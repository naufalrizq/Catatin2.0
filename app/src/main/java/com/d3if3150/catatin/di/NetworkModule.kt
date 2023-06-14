package com.d3if3150.catatin.di

import com.d3if3150.catatin.remote.Api
import com.d3if3150.catatin.repo.RealEstateRepository
import com.d3if3150.catatin.utils.network.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRealEstateRepository(
        api: Api
    ) = RealEstateRepository(api)

    @Singleton
    @Provides
    fun provideApi(): Api {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(Api::class.java)
    }

}