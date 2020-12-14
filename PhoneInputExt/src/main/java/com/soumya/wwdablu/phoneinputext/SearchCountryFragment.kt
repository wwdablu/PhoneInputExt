package com.soumya.wwdablu.phoneinputext

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soumya.wwdablu.phoneinputext.model.Country
import com.soumya.wwdablu.phoneinputext.repository.CountryRepo
import io.reactivex.rxjava3.observers.DisposableObserver
import java.util.*

class SearchCountryFragment : Fragment() {

    private var mAdapter: CountriesAdapter = CountriesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.frag_search_country, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_country_list)
        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchData()

        return view
    }

    private fun fetchData() {

        CountryRepo.fetchListOfCountries(object: DisposableObserver<LinkedList<Country>>() {
            override fun onNext(t: LinkedList<Country>?) {
                mAdapter.setData(t ?: LinkedList())
            }

            override fun onError(e: Throwable?) {
                //
            }

            override fun onComplete() {
                //
            }
        })
    }
}