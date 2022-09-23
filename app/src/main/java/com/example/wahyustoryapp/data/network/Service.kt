package com.example.wahyustoryapp.data.network

import com.example.wahyustoryapp.BuildConfig
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun getLoginData(
        @Body form: LoginForm
    ): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(
        @Body form: RegisterForm
    ): Response<NormalResponse>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Response<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<NormalResponse>
}

object ApiConfig {
    fun getApiService(): ApiService {
        val baseUrl = "https://story-api.dicoding.dev/v1/"
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = run {
            if (BuildConfig.DEBUG) {
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            } else {
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

