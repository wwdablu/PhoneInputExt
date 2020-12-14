package com.soumya.wwdablu.phoneinputext

import com.soumya.wwdablu.phoneinputext.model.Country
import java.io.Serializable

interface CountryChangeListener : Serializable {
    fun onCountrySelected(country: Country)
}