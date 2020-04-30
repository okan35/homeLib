package com.homelib.bookTemplateOpenLibrary
import com.google.gson.annotations.SerializedName

data class Authors (

	@SerializedName("name") val name : String,
	@SerializedName("key") val key : String
)