package com.example.servicesforhome.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import android.graphics.BitmapFactory
import android.graphics.Color
import com.google.android.gms.maps.model.*

import android.widget.LinearLayout
import android.view.View
import kotlinx.android.synthetic.main.employee_info.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.location.Location
import com.example.servicesforhome.R
import com.google.android.gms.common.api.GoogleApiClient


class ServiceProvider : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {

    private var sheetBehavior: BottomSheetBehavior<*>? = null

    private lateinit var mMap: GoogleMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val options = MarkerOptions()
    private val latlngs = ArrayList<LatLng>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_provider)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        request_payments.setOnClickListener {
            showDialog()
        }
        if (sheetBehavior?.state   == BottomSheetBehavior.STATE_EXPANDED) {
            bottom_search_bar.visibility = View.VISIBLE
            sheetBehavior?.state =BottomSheetBehavior.STATE_COLLAPSED
        }




    }

    override fun onInfoWindowClick(p0: Marker?) {
        if (sheetBehavior?.state   != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior?.state =BottomSheetBehavior.STATE_EXPANDED
            bottom_search_bar.visibility = View.INVISIBLE
        } else {
            bottom_search_bar.visibility = View.VISIBLE
            sheetBehavior?.state =BottomSheetBehavior.STATE_COLLAPSED
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        mMap.setOnMyLocationClickListener(onMyLocationClickListener)
        //mMap.isMyLocationEnabled = true
        mMap.setMinZoomPreference(6.0f)
        mMap.setMaxZoomPreference(12.0f)

        enableMyLocationIfPermitted()


        mMap.uiSettings.isZoomControlsEnabled = true

        latlngs.add(LatLng(-1.334343, 36.53434))
        latlngs.add(LatLng(-1.334343, 36.63434))


        val icon = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.marker_icon
        )

        for (point in latlngs) {
            options.position(point)
            options.title("someTitle")
            options.snippet("someDesc")
            options.icon(BitmapDescriptorFactory.fromBitmap(icon))
            options
            googleMap.addMarker(options)
        }

    }


    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            mMap.isMyLocationEnabled = true
        }
    }

    private fun showDefaultLocation() {
        Toast.makeText(
            this, "Location permission not granted, " + "showing default location",
            Toast.LENGTH_SHORT
        ).show()
        val redmond = LatLng(-1.334343, 36.53434)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted()
                } else {
                    showDefaultLocation()
                }
                return
            }
        }
    }

    private val onMyLocationButtonClickListener = GoogleMap.OnMyLocationButtonClickListener {

        false
    }

    private val onMyLocationClickListener =
        GoogleMap.OnMyLocationClickListener { location ->


            val circleOptions = CircleOptions()
            circleOptions.center(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            )

            circleOptions.radius(200.0)
            circleOptions.fillColor(Color.TRANSPARENT)
            circleOptions.strokeWidth(6f)

            mMap.addCircle(circleOptions)
        }

    fun showDialog() {
        val fragmentManager = supportFragmentManager
        val newFragment = PaymentDialoge()
        newFragment.show(fragmentManager, "dialog")

        /*if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog

        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }*/
    }


}
