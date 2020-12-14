package com.soumya.wwdablu.phoneinputext.repository

import com.soumya.wwdablu.phoneinputext.model.Country
import com.soumya.wwdablu.phoneinputext.network.CountriesInfoProvider
import com.soumya.wwdablu.phoneinputext.network.model.CountryInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.util.*

object CountryRepo {

    private val mCountryList: LinkedList<Country> = LinkedList()

    fun fetchListOfCountries(observer: DisposableObserver<LinkedList<Country>>) {

        if(mCountryList.size != 0) {
            observer.onNext(mCountryList)
            observer.onComplete()
            return
        }

        CountriesInfoProvider.call().fetchAllCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableObserver<List<CountryInfo>>() {
                    override fun onNext(t: List<CountryInfo>?) {

                        mCountryList.clear()
                        t?.forEach {

                            val callingCode = if(it.callingCodes[0].isEmpty() || it.callingCodes[0].isBlank()) {
                                ""
                            } else {
                                try {
                                    it.callingCodes[0]
                                } catch (ex: Exception) {
                                    ""
                                }
                            }

                            if(callingCode.isNotEmpty()) {
                                mCountryList.add(
                                        Country(
                                            it.name,
                                            it.nativeName,
                                            callingCode,
                                            it.code2,
                                            it.code3,
                                            null,
                                            it.flagSvgUrl
                                        )
                                )
                            }

                            observer.onNext(mCountryList)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        observer.onError(e)
                    }

                    override fun onComplete() {
                        observer.onComplete()
                    }

                })
    }
}