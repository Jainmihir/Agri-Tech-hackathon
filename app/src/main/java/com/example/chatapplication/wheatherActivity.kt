package com.example.chatapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.ads.AdView
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.example.chatapplication.R
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.initialization.InitializationStatus
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import com.android.volley.VolleyError
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdRequest
import java.text.DecimalFormat

class wheatherActivity() : AppCompatActivity() {
    var etCity: EditText? = null
    var etCountry: EditText? = null
    var tvResult: TextView? = null
    private val url = "https://api.openweathermap.org/data/2.5/weather"
    private val appid = "e53301e27efa0b66d05045d91b2742d3"
    var df = DecimalFormat("#.##")
    private var mAdView: AdView? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheather)
        MobileAds.initialize(this) { }
        mAdView = findViewById(R.id.adViewMain)
        val adRequest = AdRequest.Builder().build()
        with(mAdView) {
            this?.loadAd(adRequest)
        }
        etCity = findViewById(R.id.etCity)
        etCountry = findViewById(R.id.etCountry)
        tvResult = findViewById(R.id.tvResult)
    }

    fun getWeatherDetails(view: View?) {
        var tempUrl = ""
        val city = etCity!!.text.toString().trim { it <= ' ' }
        val country = etCountry!!.text.toString().trim { it <= ' ' }
        if (city == "") {
            tvResult!!.text = "City field can not be empty!"
        } else {
            tempUrl = if (country != "") {
                "$url?q=$city,$country&appid=$appid"
            } else {
                "$url?q=$city&appid=$appid"
            }
            val stringRequest = StringRequest(Request.Method.POST, tempUrl, { response ->
                var output = ""
                try {
                    val jsonResponse = JSONObject(response)
                    val jsonArray = jsonResponse.getJSONArray("weather")
                    val jsonObjectWeather = jsonArray.getJSONObject(0)
                    val description = jsonObjectWeather.getString("description")
                    val jsonObjectMain = jsonResponse.getJSONObject("main")
                    val temp = jsonObjectMain.getDouble("temp") - 273.15
                    val feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15
                    val pressure = jsonObjectMain.getInt("pressure").toFloat()
                    val humidity = jsonObjectMain.getInt("humidity")
                    val jsonObjectWind = jsonResponse.getJSONObject("wind")
                    val wind = jsonObjectWind.getString("speed")
                    val jsonObjectClouds = jsonResponse.getJSONObject("clouds")
                    val clouds = jsonObjectClouds.getString("all")
                    val jsonObjectSys = jsonResponse.getJSONObject("sys")
                    val countryName = jsonObjectSys.getString("country")
                    val cityName = jsonResponse.getString("name")
                    tvResult!!.setTextColor(Color.rgb(68, 134, 199))
                    output += """Current weather of $cityName ($countryName)
 Temp: ${df.format(temp)} °C
 Feels Like: ${df.format(feelsLike)} °C
 Humidity: $humidity%
 Description: $description
 Wind Speed: ${wind}m/s (meters per second)
 Cloudiness: $clouds%
 Pressure: $pressure hPa"""
                    tvResult!!.text = output
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error ->
                Toast.makeText(
                    applicationContext,
                    error.toString().trim { it <= ' ' },
                    Toast.LENGTH_SHORT
                ).show()
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }

    fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<wheatherActivity> {
        override fun createFromParcel(parcel: Parcel): wheatherActivity {
            return wheatherActivity(parcel)
        }

        override fun newArray(size: Int): Array<wheatherActivity?> {
            return arrayOfNulls(size)
        }
    }
}