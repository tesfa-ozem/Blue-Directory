package com.example.servicesforhome.http

import android.app.Application
import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.lang.Error


object Api: Application(){
    var URL = "http://52.226.66.148:8080/api/"
    var mGson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setPrettyPrinting()
        .setLenient()
        .serializeNulls()
        .create()
    var POST = Request.Method.POST
    var GET = Request.Method.GET

    var MY_SOCKET_TIMEOUT_MS = 50000

    fun getVolley(
        context: Application?,
        requestMethod: Int,
        function: String,
        jsonRequest: String?,
        callback: VolleyCallback,
        apiUrl:String = URL,
        form_data: Map<String, String>? = null
    ) {

        if (context != null) {
            val requestQueue = Volley.newRequestQueue(context)
            val url = apiUrl + function
            Log.i("### URL", url)
            Log.i("### REQUEST", jsonRequest)
            Log.i("###", form_data.toString())

            val stringRequest = object : StringRequest(requestMethod, url, Response.Listener { response ->
                Log.i("### RESPONSE", response)

                callback.onSuccess(response)
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                callback.onError(error)
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()


                    return params
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }


                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        jsonRequest?.toByteArray(charset("utf-8"))
                    } catch (uee: Exception) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonRequest, "utf-8")
                        null
                    }

                }

                override fun getParams(): Map<String, String>? {
                    return form_data
                }

            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            requestQueue.add(stringRequest)
        }


    }

    fun getVolley(context: Application?,
                  requestMethod: Int,
                  function: String,
                  callback: VolleyCallback,
                  apiUrl:String,
                  form_data: Map<String, String>?){
        if (context != null) {
            val requestQueue = Volley.newRequestQueue(context)
            val url = apiUrl + function
            Log.i("### URL", url)


            val stringRequest = object : StringRequest(
                requestMethod,
                url,
                Response.Listener { response ->
                    Log.i("### RESPONSE", response)

                    callback.onSuccess(response)
                },
                Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()


                    return params
                }


                override fun getParams(): Map<String, String> {

                    return form_data!!
                }

            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            requestQueue.add(stringRequest)
        }
    }

    interface VolleyCallback {
        fun onSuccess(result: String)
        fun onError(error: VolleyError)
    }

    fun save(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences("com.aw.enformentclamping.pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getValue(context: Context, key: String): String? {
        //if 1st time , register the user
        val prefs = context.getSharedPreferences("com.aw.enformentclamping.pref", Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    fun clearValue(context: Context, key: String) {
        val settings = context.getSharedPreferences("com.aw.enformentclamping.pref", Context.MODE_PRIVATE)
        settings.edit().remove(key).apply()
    }
}