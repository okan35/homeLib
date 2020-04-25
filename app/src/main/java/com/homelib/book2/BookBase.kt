package com.homelib.book2
import com.google.gson.annotations.SerializedName

data class BookBase (
	@SerializedName("totalItems") val totalItems : Int,
	@SerializedName("items") val items : List<Items>
)