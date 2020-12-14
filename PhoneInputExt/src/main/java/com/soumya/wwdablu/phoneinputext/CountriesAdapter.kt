package com.soumya.wwdablu.phoneinputext

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soumya.wwdablu.phoneinputext.databinding.ItemCountryInfoBinding
import com.soumya.wwdablu.phoneinputext.model.Country
import java.util.*

internal class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private var mCountryList: LinkedList<Country> = LinkedList()

    fun setData(list: LinkedList<Country>) {
        mCountryList = list
        notifyDataSetChanged()
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

    inner class CountryViewHolder(viewBinding: ItemCountryInfoBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        private val mViewBinding: ItemCountryInfoBinding = viewBinding

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
}