package com.adityaanugrah.skripsiku.retrofit

import com.adityaanugrah.skripsiku.model.MainModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndpoint {
    @GET("wisata")
    fun getWisata(): Call<MainModel>
}