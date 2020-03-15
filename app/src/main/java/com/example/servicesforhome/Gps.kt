package com.example.servicesforhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servicesforhome.Ui.ServiceProvider
import kotlinx.android.synthetic.main.content_gps.*


class Gps : AppCompatActivity() {
    var locationManager: LocationManager? = null
    var GpsStatus: Boolean = false
    var isNetworkEnabled: Boolean = false
    private val GPS_ENABLE_REQUEST = 0x1001
    private val WIFI_ENABLE_REQUEST = 0x1006
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)
        CheckGpsStatus()
        if(GpsStatus){
            startActivity(Intent(this,
                ServiceProvider::class.java))

            // close this activity
            finish()
        }
        enable_location.setOnClickListener {
            CheckGpsStatus()
            if(GpsStatus){
                startActivity(Intent(this,
                    ServiceProvider::class.java))

                // close this activity
                finish()
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }


        }
        no_thanks.setOnClickListener {
            startActivity(Intent(this,
                ServiceProvider::class.java))

            // close this activity
            finish()
        }



    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        startActivity(Intent(this,
            ServiceProvider::class.java))
        finish()
    }
    fun CheckGpsStatus() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        GpsStatus = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!


    isNetworkEnabled =
        locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}
