package com.example.wahyustoryapp.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

//base url : https://story-api.dicoding.dev/

interface ApiConfig {
    fun getApiService() : ApiService{
        val baseUrl = "https://story-api.dicoding.dev/"
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}