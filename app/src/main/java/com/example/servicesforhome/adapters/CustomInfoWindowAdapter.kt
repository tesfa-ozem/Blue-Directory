package com.example.servicesforhome.adapters
import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.servicesforhome.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker



class CustomInfoWindowAdapter(mainContext: Activity) : GoogleMap.InfoWindowAdapter {
    private  var  context: Activity = mainContext


    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = context.layoutInflater.inflate(R.layout.info_window, null)
        view.setOnClickListener{
            Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
        }
        val tvTitle = view.findViewById(R.id.tv_title) as TextView
        val tvSubTitle = view.findViewById(R.id.tv_subtitle) as TextView

        tvTitle.text = marker.title
        tvSubTitle.text = marker.snippet

        return view
    }

}




