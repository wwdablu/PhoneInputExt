package com.soumya.wwdablu.phoneinputext

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import com.soumya.wwdablu.phoneinputext.model.Country
import com.soumya.wwdablu.phoneinputext.repository.CountryRepo
import io.reactivex.rxjava3.observers.DisposableObserver
import java.util.*

class PhoneInputExt : CardView {

    private enum class DataMode {
        FETCH,
        PROVIDED
    }

    private val TAG: String = PhoneInputExt::class.java.simpleName
    private var mCountryList: LinkedList<Country> = LinkedList()

    private var mDataMode: DataMode = DataMode.FETCH
    private var mAutoPopulate: Boolean = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
        extractAttribInfo(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView()
        extractAttribInfo(attributeSet)
    }

    private fun extractAttribInfo(attributeSet: AttributeSet) {

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.PhoneInputExt, 0, 0)
            .apply {

                try {
                    mDataMode = DataMode.values()[getInteger(R.styleable.PhoneInputExt_dataMode, 0)]
                    mAutoPopulate = getBoolean(R.styleable.PhoneInputExt_autoPopulate, false)

                    if(mDataMode == DataMode.FETCH) {
                        fetchListOfCountries()
                    }

                } finally {
                    recycle()
                }
            }
    }

    /**
     * Set the list of countries to be displayed
     * @param list LinkedList of all the countries
     */
    fun setLiftOfCountries(list: LinkedList<Country>) : Boolean {

        if(list.size == 0 || mDataMode == DataMode.FETCH) {
            return false
        }

        mCountryList.clear()
        return mCountryList.addAll(list)
    }

    private fun initView() {
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics)
        LayoutInflater.from(context).inflate(R.layout.phone_input_ext, this, true)

        findViewById<ImageView>(R.id.iv_country_flag).setOnClickListener(mClickListener)
        findViewById<AppCompatTextView>(R.id.tv_country_phone_code).setOnClickListener(mClickListener)
    }

    private fun fetchListOfCountries() {
        CountryRepo.fetchListOfCountries(object: DisposableObserver<LinkedList<Country>>() {
            override fun onNext(t: LinkedList<Country>?) {
                mCountryList = t ?: LinkedList()
            }

            override fun onError(e: Throwable?) {
                Log.e(TAG, "", e)
            }

            override fun onComplete() {
                //
            }
        })
    }

    private val mClickListener: OnClickListener = OnClickListener {

        context.startActivity(Intent(context, SearchCountryActivity::class.java))
    }
}