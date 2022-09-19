package com.example.wahyustoryapp.data.retrofit

import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST

interface ApiService {

    @Multipart
    @POST
    fun postData()


}