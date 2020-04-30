package com.homelib.bookTemplateOpenLibrary
import com.google.gson.annotations.SerializedName

data class Identifiers (

	@SerializedName("overdrive") val overdrive : List<String>
)