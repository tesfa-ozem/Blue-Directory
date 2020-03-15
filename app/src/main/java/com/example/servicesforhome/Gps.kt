package com.example.servicesforhome
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_gps.*
import android.content.Intent
import android.provider.Settings
import android.location.LocationManager
import android.app.Activity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T








class Gps : AppCompatActivity() {
    var locationManager: LocationManager? = null
    var GpsStatus: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)
        CheckGpsStatus()
        if(GpsStatus){
            startActivity(Intent(this,Dashboard::class.java))

            // close this activity
            finish()
        }
        enable_location.setOnClickListener {
            CheckGpsStatus()
            if(GpsStatus){
                startActivity(Intent(this,Dashboard::class.java))

                // close this activity
                finish()
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }


        }



    }

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//
//        }
//    }
    fun CheckGpsStatus() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        GpsStatus = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
    }

}