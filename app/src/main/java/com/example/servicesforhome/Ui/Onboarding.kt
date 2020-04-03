package com.example.servicesforhome.Ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.example.servicesforhome.R
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.models.BaseResponse
import com.example.servicesforhome.models.Users
import com.google.gson.Gson
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.json.JSONObject


class Onboarding : AppCompatActivity() {


    var spcountries: SearchableSpinner? = null
    var arraycountires = ArrayList<String>()
    var adapter: ArrayAdapter<*>? = null
    var title =""
    var msg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setSupportActionBar(toolbar)
        getProvider()
        spcountries = findViewById(R.id.search_service_category)


        capture_button.setOnClickListener {
            dispatchTakePictureIntent()
        }
        register_button.setOnClickListener {
            registerProvider(
                names_input.text.toString(),
                dob_input.text.toString(),
                phone_input.text.toString(),
                "2",
                profession_input.text.toString(),
                experience_input.text.toString(),
                kin_input.text.toString(),
                "Service"
            )
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            view_finder.setImageBitmap(imageBitmap)
        }
    }

    private fun getCategories() {
        try {
            val header = HashMap<String, String>()
            val newFragment = LoadingDialoge()
            newFragment.show(supportFragmentManager, "missiles")
            Api.getVolley(
                application, Api.GET, "categories", "", object : Api.VolleyCallback {
                    override fun onSuccess(result: String) {
                        val gson = Gson()
                        val category = gson.fromJson(result, BaseResponse::class.java)
                        for (cat in category.data) {
                            arraycountires.add(cat.name)
                        }
                        newFragment.dismiss()
                        adapter = ArrayAdapter(
                            baseContext,
                            R.layout.simple_list_item_1,
                            arraycountires
                        )
                        adapter!!.setDropDownViewResource(R.layout.simple_list_item_1)
                        spcountries!!.adapter = adapter
                        spcountries!!.setTitle("Select Service Category")
                        spcountries!!.setPositiveButton("Done")

                    }

                    override fun onError(error: VolleyError) {
                        newFragment.dismiss()
                    }
                }, Api.URL, form_data = null, headers = header
            )
        } catch (ex: Exception) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun registerProvider(
        fullNames: String,
        dob: String,
        phoneNumber: String,
        serviceId: String,
        proDetail: String,
        experience: String,
        nextOfKin: String,
        serviceDoc: String
    ) {
        val newFragment = LoadingDialoge()
        newFragment.show(supportFragmentManager,"loading")


        val header = HashMap<String, String>()
        val token = Api.getValue(baseContext,"Token")
        val creds:String = String.format("%s:%s",token,"")
        val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
        header["Authorization"] = auth
        val jsonBody = JSONObject()
        var jsonData = HashMap<String, String>();
        jsonBody.put("dob" , dob)
        jsonBody.put("name", fullNames)
        jsonBody.put("phone", phoneNumber)
        jsonBody.put("service_id", serviceId)
        jsonBody.put("professional_detail", proDetail)
        jsonBody.put("experience",experience)
        jsonBody.put("next_of_kin", nextOfKin)
        jsonBody.put("service_documentation", serviceDoc)
        jsonBody.put("is_provider",true)
        val  mRequestBody = jsonBody.toString()
        Api.getVolley(application, Api.POST, "registerProvider", mRequestBody, object : Api.VolleyCallback {
            override fun onSuccess(result: String) {
                newFragment.dismiss()
                startActivity(Intent(baseContext, ProviderProfile::class.java))
                finish()
            }

            override fun onError(error: VolleyError) {

                newFragment.dismiss()

                val gson = Gson()

            }
        }, Api.localUrl, form_data = null, headers = header)
    }

    fun getProvider(){
        val header = HashMap<String, String>()
        val token = Api.getValue(baseContext,"Token")
        val creds:String = String.format("%s:%s",token,"")
        val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
        header["Authorization"] = auth
        Api.getVolley(application,Api.POST,"getProvider","",object :Api.VolleyCallback{
            override fun onSuccess(result: String) {
                val gson = Gson()
                val user = gson.fromJson(result,Users::class.java)
                if(user.data.isProvider){
                    startActivity(Intent(baseContext, ProviderProfile::class.java))
                    finish()
                }else{
                    getCategories()
                }
            }

            override fun onError(error: VolleyError) {
                TODO("Not yet implemented")
            }
        },Api.localUrl,form_data = null,headers = header)
    }

}
