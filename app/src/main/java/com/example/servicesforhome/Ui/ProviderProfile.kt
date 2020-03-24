package com.example.servicesforhome.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.servicesforhome.R
import kotlinx.android.synthetic.main.activity_provider_profile.*

class ProviderProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_provider_profile)
        bottom_app_bar.replaceMenu(R.menu.bottom_nav_menu)

    }
}
