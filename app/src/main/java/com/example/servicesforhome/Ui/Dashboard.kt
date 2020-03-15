package com.example.servicesforhome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_dashboared.*
import kotlinx.android.synthetic.main.content_dashboard.*
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.servicesforhome.Ui.ServiceProvider


@SuppressLint("Registered")
class Dashboard : AppCompatActivity() {
    lateinit var animFadein: Animation
    lateinit var animFadeout: Animation

    lateinit var search: TextView
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboared)

        animFadein = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_in)
        animFadeout = AnimationUtils.loadAnimation(applicationContext,R.anim.fade_out)

        search_field.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    card_container.visibility = View.GONE
                    card_container.animation = animFadeout
                    // This means user has erased all the text from the edittext.
                    // For this instance you have to remove the added edittext.
                }else if(s.isEmpty()){
                    card_container.visibility = View.GONE
                    card_container.animation = animFadein
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.isEmpty()) {
                    // It means your this was your initial input before changing text
                    // add another edit text at this moment.
                }

            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        })
        cleaning_card.setOnClickListener {
            startActivity(Intent(this, ServiceProvider::class.java)) }

    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

}
