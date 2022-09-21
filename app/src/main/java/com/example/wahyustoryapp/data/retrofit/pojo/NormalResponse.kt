package com.example.wahyustoryapp.data.retrofit.pojo

import com.google.gson.annotations.SerializedName

data class NormalResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
