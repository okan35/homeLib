package com.homelib.book2
import com.google.gson.annotations.SerializedName

data class IndustryIdentifiers (

	@SerializedName("type") val type : String,
	@SerializedName("identifier") val identifier : String
)