package com.example.wahyustoryapp.data.fake

import com.example.wahyustoryapp.DataDummy
import com.example.wahyustoryapp.data.network.ApiService
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.LoginResponse
import com.example.wahyustoryapp.data.network.response.NormalResponse
import com.example.wahyustoryapp.data.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiStory : ApiService {
    override suspend fun getLoginData(form: LoginForm): Response<LoginResponse> {
        return Response.success(DataDummy.provideLoginResponse())
    }

    override suspend fun registerUser(form: RegisterForm): Response<NormalResponse> {
        return Response.success(DataDummy.provideNormalResponse())
    }

    override suspend fun getAllStory(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): Response<StoryResponse> {
        return Response.success(DataDummy.provideStoryResponse(size = size))
    }

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<NormalResponse> {
        return Response.success(DataDummy.provideNormalResponse())
    }
}