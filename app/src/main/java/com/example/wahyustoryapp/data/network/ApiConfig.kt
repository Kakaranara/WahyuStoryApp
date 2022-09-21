package com.example.wahyustoryapp.data.network

import com.example.wahyustoryapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//base url : https://story-api.dicoding.dev/

class ApiConfig {
    companion object{
        fun getApiService() : ApiService{
            val baseUrl = "https://story-api.dicoding.dev/v1/"
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = run{
                if(BuildConfig.DEBUG){
                    OkHttpClient.Builder().addInterceptor(interceptor).build()
                }else{
                    OkHttpClient.Builder().build()
                }
            }
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}