package com.example.gallerydemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: LiveData<List<PhotoItem>>
    get() = _photoListLive

    fun fetchData(){
        val stringRequest = StringRequest(
                Request.Method.GET,
                getUrl(),
                {
                    val pixabey = Gson().fromJson(it, Pixabey::class.java)
                    _photoListLive.value = pixabey.hits.toList()
                },
                {
                    Log.d(TAG, "fetchData: ${it.toString()}")
                }
        )
        VolleySingleton.getInstence(getApplication()).requestQueue.add(stringRequest)
    }

    fun getUrl(): String {
        val keywords = arrayOf("cat","dog","car","beauty","flowers","animal","view")
        return "https://pixabay.com/api/?key=20961543-acdb0a829224bfb7243777df2&q=${keywords.random()}&image_type=photo&pretty=true"
    }
}