package com.example.servicesforhome.Ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.servicesforhome.Access
import com.example.servicesforhome.R
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=3000 // 3 sec
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

            startActivity(Intent(this,
                Access::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}
