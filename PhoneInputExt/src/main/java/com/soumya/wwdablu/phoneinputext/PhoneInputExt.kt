package com.soumya.wwdablu.phoneinputext

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
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
    private lateinit var mHandler: Handler

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

        mHandler = Handler()

        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics)
        LayoutInflater.from(context).inflate(R.layout.phone_input_ext, this, true)

        findViewById<ImageView>(R.id.iv_country_flag).setOnClickListener(mClickListener)
        updateCountryFlag(getDeviceCountryCode())

        findViewById<AppCompatTextView>(R.id.tv_country_phone_code).setOnClickListener(mClickListener)
    }

    private fun updateCountryFlag(code: String) {

        val imageView = findViewById<ImageView>(R.id.iv_country_flag)
        imageView.setOnClickListener(mClickListener)
        Glide.with(context)
                .asBitmap()
                .load("https://www.countryflags.io/$code/flat/64.png")
                .into(imageView)
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

                val code2 = getDeviceCountryCode()
                val currentCountry: Country? = mCountryList.find {
                    it.countryCode2.equals(code2, true)
                }

                findViewById<AppCompatTextView>(R.id.tv_country_phone_code).text = "${currentCountry?.countryCallingCode}"
            }
        })
    }

    private fun getDeviceCountryCode() : String {

        val telMgr: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                ?: return "IN"

        return when (telMgr.simState) {

            TelephonyManager.SIM_STATE_ABSENT -> {
                TimeZone.getDefault().id
            }

            TelephonyManager.SIM_STATE_READY -> {
                telMgr.networkCountryIso
            }

            else -> {
                "IN"
            }
        }
    }

    private val mCountryChangeListener: CountryChangeListener = object: CountryChangeListener {

        override fun onCountrySelected(country: Country) {
            mHandler.post {
                updateCountryFlag(country.countryCode2)
            }
        }
    }

    private val mClickListener: OnClickListener = OnClickListener {

        val intent: Intent = Intent(context, SearchCountryActivity::class.java)
        //intent.putExtra("callback", mCountryChangeListener)
        context.startActivity(intent)
    }
}