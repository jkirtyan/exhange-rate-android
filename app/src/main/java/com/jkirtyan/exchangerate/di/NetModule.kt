package com.jkirtyan.exchangerate.di

import com.jkirtyan.exchangerate.ExchangeRateService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetModule {
    private val BASE_URL = "https://revolut.duckdns.org/"

    @Provides
    @Singleton
    fun provideExchangeRateService(): ExchangeRateService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create<ExchangeRateService>(ExchangeRateService::class.java)
    }
}