package com.soumya.wwdablu.phoneinputext.model

import android.graphics.drawable.Drawable

class Country(name: String,
              nativeName: String,
              callingCode: String,
              code2: String,
              code3: String,
              flagDrawable: Drawable?,
              flagSource: String?) {

    var countryName: String = name
        set(value) {
            field = if(value.isBlank() || value.isEmpty()) {
                ""
            } else {
                value
            }
        }

    var countryNativeName: String = name
        set(value) {
            field = if(value.isBlank() || value.isEmpty()) {
                ""
            } else {
                value
            }
        }

    var countryCode2: String = code2
        set(value) {
            field = if(value.isBlank() || value.isEmpty()) {
                ""
            } else {
                value
            }
        }

    var countryCode3: String = code3
        set(value) {
            field = if(value.isBlank() || value.isEmpty()) {
                ""
            } else {
                value
            }
        }

    var countryCallingCode: String = callingCode
        set(value) {
            field = if(value.isBlank() || value.isEmpty()) {
                ""
            } else {
                value
            }
        }
        get() {
            return "+$field"
        }

    var countryFlagDrawable: Drawable? = flagDrawable
    var countryFlagSource: String? = flagSource
}
