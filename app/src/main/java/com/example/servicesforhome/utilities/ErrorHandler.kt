package com.example.servicesforhome.utilities

import com.android.volley.*

object ErrorHandler {
    fun volleyHandler(error:Any):String{
        if (error is TimeoutError || error is NoConnectionError) {

            return "Timed out"
        } else if (error is AuthFailureError) {
            return "Authentication Failed"

        } else if (error is ServerError) {
            return "An Error "

        } else if (error is NetworkError) {
            return "Network Failed to resolve"

        } else if (error is ParseError) {
            return "An Error"
        }else{
            return "Error"
        }

    }
}