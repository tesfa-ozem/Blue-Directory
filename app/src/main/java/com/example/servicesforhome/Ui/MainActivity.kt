package com.example.servicesforhome.Ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.example.servicesforhome.Access
import com.example.servicesforhome.Gps
import com.example.servicesforhome.R
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.models.LogedIn
import com.google.gson.Gson
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=2000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(
            application, "8a0c273e-2287-4e1c-bbbb-4de72137a2d7",
            Analytics::class.java, Crashes::class.java
        )
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            logIn()

        }, SPLASH_TIME_OUT)
    }
    fun logIn(){
        val header = HashMap<String,String>()
        var token = Api.getValue(baseContext,"Token")
        var creds:String = String.format("%s:%s",token,"")
        var auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
        header["Authorization"] = auth
        Api.getVolley(application, Api.POST, "login","", object : Api.VolleyCallback {
            override fun onSuccess(result: String) {
                val gson = Gson()
                val log = gson.fromJson(result, LogedIn::class.java)
                if(log.message=="Logged In"){
                    startActivity(Intent(applicationContext, Gps::class.java))
                    finish()
                }else{
                    startActivity(Intent(applicationContext,
                        Access::class.java))

                    // close this activity
                    finish()
                }


            }

            override fun onError(error: VolleyError) {

                startActivity(Intent(applicationContext,
                    Access::class.java))

                // close this activity
                finish()
            }
        }, Api.URL,form_data = null,headers = header
        )
    }
}
