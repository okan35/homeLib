package com.homelib.book2


import com.google.gson.annotations.SerializedName

data class Items (
	@SerializedName("volumeInfo") val volumeInfo : VolumeInfo
)