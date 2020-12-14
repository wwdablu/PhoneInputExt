package com.soumya.wwdablu.phoneinputext.network

import com.soumya.wwdablu.phoneinputext.network.model.CountryInfo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CountriesInfoAPI {

    @GET("/rest/v2/all")
    fun fetchAllCountries() : Observable<List<CountryInfo>>
}