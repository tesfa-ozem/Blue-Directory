package com.example.servicesforhome.utilities

import android.app.Activity
import android.app.FragmentManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog

import com.example.servicesforhome.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GpsUtils : Activity(), GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

    internal val RQS_GooglePlayServices = 1
    private val myMap: GoogleMap? = null
    internal var tvLocInfo: TextView? = null

    internal inner class MyInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        private val myContentsView: View

        init {
            myContentsView = layoutInflater.inflate(R.layout.info_window, null)
        }

        override fun getInfoContents(marker: Marker): View {
            val tvTitle = myContentsView.findViewById<View>(R.id.title) as TextView
            tvTitle.text = marker.title


            return myContentsView
        }

        override fun getInfoWindow(marker: Marker): View? {
            // TODO Auto-generated method stub
            return null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val myFragmentManager = fragmentManager
        val myMapFragment = myFragmentManager.findFragmentById(R.id.map) as MapFragment
        /* myMap = myMapFragment.getMap();*/

        myMap!!.isMyLocationEnabled = true

        myMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isCompassEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = true

        myMap.uiSettings.setAllGesturesEnabled(true)

        myMap.isTrafficEnabled = true

        myMap.setOnMapLongClickListener(this)
        myMap.setInfoWindowAdapter(MyInfoWindowAdapter())
        myMap.setOnInfoWindowClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /* switch (item.getItemId()) {
            case R.id.menu_legalnotices:
                String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
                        getApplicationContext());
                AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();
                return true;
        }*/
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)

        if (resultCode == ConnectionResult.SUCCESS) {
            Toast.makeText(
                applicationContext,
                "isGooglePlayServicesAvailable SUCCESS",
                Toast.LENGTH_LONG
            ).show()
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices)
        }
    }

    override fun onMapLongClick(point: LatLng) {
        tvLocInfo!!.text = "New marker added@$point"

        val newMarker = myMap!!.addMarker(
            MarkerOptions()
                .position(point)
                .snippet(point.toString())
        )
        newMarker.title = newMarker.id

    }

    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(
            baseContext,
            "Info Window clicked@" + marker.id,
            Toast.LENGTH_SHORT
        ).show()

    }

}