package com.soumya.wwdablu.phoneinputext

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soumya.wwdablu.phoneinputext.model.Country

class SearchCountryFragment(listener: CountryListener) : Fragment(), CountriesAdapter.CountryListener {

    interface CountryListener {
        fun onCountrySelected(country: Country)
    }

    private var mAdapter: CountriesAdapter = CountriesAdapter(this)
    private val mListener: CountryListener = listener
    private lateinit var mSearchCountryEditText: AppCompatEditText
    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.frag_search_country, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_country_list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        mSearchCountryEditText = view.findViewById(R.id.et_search_country)
        mSearchCountryEditText.addTextChangedListener(mTextWatcher)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSearchCountryEditText.removeTextChangedListener(mTextWatcher)
    }

    override fun onCountrySelected(country: Country) {
        mListener.onCountrySelected(country)
    }

    private val mTextWatcher: TextWatcher = object: TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mHandler.removeCallbacks(mSearchRunnable)
            mHandler.postDelayed(mSearchRunnable, if (s.isNullOrBlank() || s.isNullOrEmpty()) 0 else 250)
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }

    }

    private val mSearchRunnable: Runnable = Runnable {

        if(!isDetached || !isRemoving) {
            mAdapter.searchAndShow(mSearchCountryEditText.text.toString())
        }
    }
}