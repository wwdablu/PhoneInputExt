package com.soumya.wwdablu.phoneinputextsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.soumya.wwdablu.phoneinputext.PhoneInputExt
import com.soumya.wwdablu.phoneinputext.model.Country
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set dataMode to provided from fetch in XML
        //provideOwnData()

        findViewById<Button>(R.id.btn_fetch).setOnClickListener {

            val pair: Pair<String, String> = findViewById<PhoneInputExt>(R.id.pie_phone).getSelectedData()
            Toast.makeText(this, pair.first + " " + pair.second, Toast.LENGTH_SHORT).show()
        }
    }

    private fun provideOwnData() {
        val list: LinkedList<Country> = LinkedList()

        list.add(Country("India", "ভারত", "91", "IN", "IND", null, "https://www.countryflags.io/IN/flat/64.png"))
        list.add(Country("United States of America", "United States of America", "1", "US", "USA", null, "https://www.countryflags.io/US/flat/64.png"))
        list.add(Country("Japan", "日本", "81", "JP", "JPN", null, "https://www.countryflags.io/JP/flat/64.png"))

        findViewById<PhoneInputExt>(R.id.pie_phone).setLiftOfCountries(list)
    }
}