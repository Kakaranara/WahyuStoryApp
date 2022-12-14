package com.example.wahyustoryapp.data.fake

import com.example.wahyustoryapp.AuthDummy
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
        return Response.success(AuthDummy.provideLoginResponse())
    }

    override suspend fun registerUser(form: RegisterForm): Response<NormalResponse> {
        return Response.success(AuthDummy.provideRegisterResponse())
    }

    override suspend fun getAllStory(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): Response<StoryResponse> {
        return if (location == 1) {
            Response.success(DataDummy.provideStoryResponse(true, size = size))
        } else
            Response.success(DataDummy.provideStoryResponse(false, size = size))

    }

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<NormalResponse> {
        return Response.success(DataDummy.provideNormalResponse())
    }

    override suspend fun uploadImageWithLocation(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ): Response<NormalResponse> {
        return Response.success(DataDummy.provideNormalResponse())
    }
}