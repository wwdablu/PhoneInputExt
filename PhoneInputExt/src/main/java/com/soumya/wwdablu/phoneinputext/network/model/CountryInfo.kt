package com.soumya.wwdablu.phoneinputext.network.model

import com.google.gson.annotations.SerializedName

data class CountryInfo(@SerializedName("name") val name: String,
                       @SerializedName("alpha2Code") val code2: String,
                       @SerializedName("alpha3Code") val code3: String,
                       @SerializedName("callingCodes") val callingCodes: List<String>,
                       @SerializedName("nativeName") val nativeName: String,
                       @SerializedName("flag") val flagSvgUrl: String)
