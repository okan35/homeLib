package com.homelib.bookTemplateGoogle


import com.google.gson.annotations.SerializedName

data class Items (
	@SerializedName("volumeInfo") val volumeInfo : VolumeInfo
)