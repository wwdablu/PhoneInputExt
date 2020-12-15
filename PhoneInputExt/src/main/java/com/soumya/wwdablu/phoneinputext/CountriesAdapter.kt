package com.soumya.wwdablu.phoneinputext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soumya.wwdablu.phoneinputext.databinding.ItemCountryInfoBinding
import com.soumya.wwdablu.phoneinputext.model.Country
import com.soumya.wwdablu.phoneinputext.repository.CountryRepo
import io.reactivex.rxjava3.observers.DisposableObserver
import java.util.*

internal class CountriesAdapter(listener: CountryListener) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    interface CountryListener {
        fun onCountrySelected(country: Country)
    }

    private var mCountryList: LinkedList<Country> = LinkedList()
    private var mOriginalCountryList: LinkedList<Country> = LinkedList()
    private var mCountryListener: CountryListener = listener

    init {
        fetchData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        val viewBinding: ItemCountryInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_country_info, parent, false)

        return CountryViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(mCountryList[position])
    }

    override fun getItemCount(): Int {
        return mCountryList.size
    }

    fun searchAndShow(search: String) {

        val list: List<Country> = if(search.isEmpty() || search.isBlank()) {
            mOriginalCountryList
        } else {
            mOriginalCountryList.filter {
                it.countryName.startsWith(search, true)
            }
        }

        mCountryList.clear()
        mCountryList.addAll(list)
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(viewBinding: ItemCountryInfoBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        private val mViewBinding: ItemCountryInfoBinding = viewBinding

        init {
            mViewBinding.root.setOnClickListener {

                val country = mCountryList[adapterPosition]
                CountryRepo.setUserSelectedCountry(country)
                mCountryListener.onCountrySelected(country)
            }
        }

        fun bind(country: Country) {
            mViewBinding.country = country

            if(country.countryFlagSource != null && country.countryFlagSource!!.isNotEmpty() && country.countryFlagSource!!.isNotBlank()) {

                Glide.with(mViewBinding.ivCountryFlag.context)
                        .asBitmap()
                        .load("https://www.countryflags.io/" + country.countryCode2 + "/flat/64.png")
                        .into(mViewBinding.ivCountryFlag)
            } else {
                mViewBinding.ivCountryFlag.setImageDrawable(country.countryFlagDrawable)
            }
        }
    }

    private fun fetchData() {

        CountryRepo.fetchListOfCountries(object: DisposableObserver<LinkedList<Country>>() {
            override fun onNext(t: LinkedList<Country>?) {
                mCountryList = t ?: LinkedList()
                mOriginalCountryList.clear()
                mOriginalCountryList.addAll(mCountryList)
            }

            override fun onError(e: Throwable?) {
                //
            }

            override fun onComplete() {
                notifyDataSetChanged()
            }
        })
    }
}