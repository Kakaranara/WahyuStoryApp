package com.example.wahyustoryapp

import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.data.network.LoginForm
import com.example.wahyustoryapp.data.network.RegisterForm
import com.example.wahyustoryapp.data.network.response.*

object DataDummy {


    fun provideNormalResponse(): NormalResponse {
        return NormalResponse(false, "ok")
    }

    fun provideStoryResponse(withLocation : Boolean = false, page: Int = 1, size: Int? = 10): StoryResponse {
        if(withLocation){
            return provideStoryResponseWithLocation(size = size)
        }
        return StoryResponse(
            List(size ?: 10) {
                ListStoryItem(
                    photoUrl = "fff",
                    createdAt = "2022-10-20T08:43:28.344Z",
                    name = "andre",
                    description = "desc",
                    lon = null,
                    id = "ID$it",
                    lat = null
                )
            },
            false,
            "false"
        )
    }
    private fun provideStoryResponseWithLocation(token: String = "", page: Int = 1, size: Int? = 10): StoryResponse {
        return StoryResponse(
            List(size ?: 10) {
                ListStoryItem(
                    photoUrl = "fff",
                    createdAt = "2022-10-20T08:43:28.344Z",
                    name = "andre",
                    description = "desc",
                    lon = 525.35 + (it / 100),
                    id = "ID$it",
                    lat = 545.24 + (it / 100)
                )
            },
            false,
            "false"
        )
    }

    fun provideStoryList(size: Int? = null): List<Story> {
        return List(size ?: 10) {
            Story(
                0,
                photoUrl = "fff",
                createdAt = "Thursday, October 20, 2022 at 3:03:44 PM Indochina Time",
                name = "andre",
                description = "desc",
                lon = null,
                id = "ID$it",
                lat = null
            )
        }
    }

    fun provideStoryListWithLocation(size: Int? = null): List<Story> {
        return List(size ?: 10) {
            Story(
                0,
                photoUrl = "fff",
                createdAt = "Thursday, October 20, 2022 at 3:03:44 PM Indochina Time",
                name = "andre",
                description = "desc",
                lon = 525.35 + (it / 100),
                id = "ID$it",
                lat = 545.24 + (it / 100)
            )
        }
    }

}

object AuthDummy {
    fun provideLoginForm(): LoginForm = LoginForm("a@gmail.com", "123456")


    fun provideRegisterResponse() : NormalResponse = NormalResponse(false, "ok")

    fun provideRegisterForm(): RegisterForm =
        RegisterForm("debugk", "wahyuKoco@gmail.com", "123456")

    fun provideLoginResponse(): LoginResponse =
        LoginResponse(LoginResult("", "IDK412", "ABCD"), false, "ok")
}