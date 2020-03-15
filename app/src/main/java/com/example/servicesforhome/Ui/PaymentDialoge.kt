package com.example.servicesforhome.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.servicesforhome.R
import kotlinx.android.synthetic.main.paymets_dialoge_layout.view.*

class PaymentDialoge: DialogFragment(){
    lateinit var mainview: View
    val params = HashMap<String, String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainview = inflater.inflate(R.layout.paymets_dialoge_layout, container, false)
        // Inflate the layout to use as dialog or embedded fragment
        mainview.button2.setOnClickListener {
            sendPush()
        }
        return  mainview
    }

    /** The system calls this only when creating the layout in a dialog. */


    fun  sendPush(){
        params["TransactionType"] = "CustomerPayBillOnline"
        params["PayBillNumber"] = "825445"
        params["Amount"] = "2"
        params["PhoneNumber"] = "0727292911"
        params["AccountReference"] = "PKX2019062"
        params["TransactionDesc"] = "PKX201906264"
        params["FullNames"] = "- - -"

//        Api.getVolley(context,Api.POST,"", object : Api.VolleyCallback {
//            override fun onSuccess(result: String) {
//
//            }
//        },apiUrl = Api.URL,form_data = params)
    }

}