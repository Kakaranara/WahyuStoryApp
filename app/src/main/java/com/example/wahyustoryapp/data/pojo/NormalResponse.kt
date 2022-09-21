package com.example.wahyustoryapp.data.pojo

import com.google.gson.annotations.SerializedName

data class NormalResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
