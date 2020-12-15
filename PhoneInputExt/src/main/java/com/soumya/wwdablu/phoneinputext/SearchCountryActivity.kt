package com.soumya.wwdablu.phoneinputext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soumya.wwdablu.phoneinputext.model.Country

class SearchCountryActivity : AppCompatActivity(), SearchCountryFragment.CountryListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_country)

        val fragment = SearchCountryFragment(this)

        supportFragmentManager.beginTransaction()
                .add(R.id.fl_search_frag_container, fragment, SearchCountryFragment::class.java.simpleName)
                .addToBackStack(SearchCountryFragment::class.java.simpleName)
                .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) {
            finish()
            return
        }

        super.onBackPressed()
    }

    override fun onCountrySelected(country: Country) {
        finish()
    }
}