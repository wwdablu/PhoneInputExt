package com.soumya.wwdablu.phoneinputext

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
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

    private var mDataMode: DataMode = DataMode.FETCH
    private var mAutoPopulate: Boolean = false
    private lateinit var mHandler: Handler

    private lateinit var mCountryCodeTextView: AppCompatTextView
    private lateinit var mPhoneNumberEditText: AppCompatEditText
    private lateinit var mCountryFlagImageView: ImageView
    private lateinit var mClearImageButton: ImageButton

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

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        val country: Country? = CountryRepo.getUserSelectedCountry()
        if(country != null) {
            updateCountryFlag(country)
            mCountryCodeTextView.text = country.countryCallingCode
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPhoneNumberEditText.removeTextChangedListener(mPhoneInputWatcher)
    }

    /**
     * Returns the value provided by the user in the UI control
     * @return Pair<countryCode: String, phoneNumber: String>
     */
    fun getSelectedData() : Pair<String, String> {
        return Pair(mCountryCodeTextView.text.toString(), mPhoneNumberEditText.text.toString())
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
    fun setLiftOfCountries(list: LinkedList<Country>) {

        if(list.size == 0 || mDataMode == DataMode.FETCH) {
            return
        }

        CountryRepo.setUserDefinedCountryList(list)

        val code = getDeviceCountryCode()
        list.find {
            it.countryCode2.equals(code, true)
        }?.let {
            updateCountryFlag(it)
            mCountryCodeTextView.text = it.countryCallingCode
        }
    }

    private fun initView() {

        mHandler = Handler()

        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics)
        LayoutInflater.from(context).inflate(R.layout.phone_input_ext, this, true)

        mCountryFlagImageView = findViewById(R.id.iv_country_flag)
        mCountryFlagImageView.setOnClickListener(mClickListener)

        mCountryCodeTextView = findViewById(R.id.tv_country_phone_code)
        mCountryCodeTextView.setOnClickListener(mClickListener)

        mPhoneNumberEditText = findViewById(R.id.et_phone_number)
        mPhoneNumberEditText.addTextChangedListener(mPhoneInputWatcher)

        mClearImageButton = findViewById(R.id.ib_clear)
        mClearImageButton.setOnClickListener {
            mPhoneNumberEditText.text?.clear()
        }
    }

    private fun updateCountryFlag(country: Country) {

        if(mDataMode == DataMode.PROVIDED) {

            if(country.countryFlagDrawable != null) {
                mCountryFlagImageView.setImageDrawable(country.countryFlagDrawable)
                return
            }
        }

        val imageUrl = if(mDataMode == DataMode.PROVIDED) {
            country.countryFlagSource
        } else {
            "https://www.countryflags.io/${country.countryCode2}/flat/64.png"
        }

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(mCountryFlagImageView)
    }

    private fun fetchListOfCountries() {
        CountryRepo.fetchListOfCountries(object: DisposableObserver<LinkedList<Country>>() {
            override fun onNext(t: LinkedList<Country>?) {
                val code2 = getDeviceCountryCode()
                val currentCountry: Country? = t?.find {
                    it.countryCode2.equals(code2, true)
                }

                mCountryCodeTextView.text = "${currentCountry?.countryCallingCode}"

                if(currentCountry != null) {
                    updateCountryFlag(currentCountry)
                }
            }

            override fun onError(e: Throwable?) {
                Log.e(TAG, "", e)
            }

            override fun onComplete() {
                //
            }
        })
    }

    private fun getDeviceCountryCode() : String {

        val telMgr: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
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

    private val mClickListener: OnClickListener = OnClickListener {

        context.startActivity(Intent(context, SearchCountryActivity::class.java))
    }

    private val mPhoneInputWatcher: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            mClearImageButton.visibility  = if(s.toString().isEmpty() || s.toString().isBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }

    }
}