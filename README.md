# PhoneInputExt
Android custom view which allows the user to display the phone input field containing the flag of the current country with the country code. Allows the user to select from list of countries with the country code.

```
<com.soumya.wwdablu.phoneinputext.PhoneInputExt
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:id="@+id/pie_phone"
    android:layout_margin="40dp"
    app:dataMode="fetch"
    />
```

The control provides two (2) method by which the list of supported countries are displayed:

* Fetch
* Provided  

In the fetch mode, the control will use the API to fetch the list of countries and its details from https://restcountries.eu/. Once these data are obtained it would then dsplay the flags of those countries based on the ISO code from https://www.countryflags.io/.  

If the developer wants, they can provide a list of Country to be displayed. For example, the below code will show only India, USA and Japan.  
```
val list: LinkedList<Country> = LinkedList()

list.add(Country("India", "ভারত", "91", "IN", "IND", null, "https://www.countryflags.io/IN/flat/64.png"))
list.add(Country("United States of America", "United States of America", "1", "US", "USA", null, "https://www.countryflags.io/US/flat/64.png"))
list.add(Country("Japan", "日本", "81", "JP", "JPN", null, "https://www.countryflags.io/JP/flat/64.png"))

findViewById<PhoneInputExt>(R.id.pie_phone).setLiftOfCountries(list)
```  

Sample:  
![Sample Implementation](https://github.com/wwdablu/PhoneInputExt/blob/master/sample/PhoneInputExt_v1.gif)
