package com.soumya.wwdablu.phoneinputext.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal object CountriesInfoProvider {

    private val mAPI: CountriesInfoAPI

    init {
        mAPI = Retrofit.Builder()
            .baseUrl("https://restcountries.eu/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesInfoAPI::class.java)
    }

    fun call() : CountriesInfoAPI {
        return mAPI
    }
}