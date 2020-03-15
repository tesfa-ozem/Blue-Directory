package com.example.servicesforhome.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.models.BaseResponse
import com.example.servicesforhome.models.Category


class CategoryViewModel(application: Application): AndroidViewModel(application) {
    var data  = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = loadCategory()

    private fun loadCategory(): LiveData<List<Category>> {
        Api.getVolley(getApplication(),Api.GET,"categories","",object :Api.VolleyCallback{
            override fun onSuccess(result: String){
                Log.i("response",result)


                data.value = Api.mGson.fromJson(result,BaseResponse::class.java).data

            }

            override fun onError(error: VolleyError) {

            }
        })
        return  data
    }
}