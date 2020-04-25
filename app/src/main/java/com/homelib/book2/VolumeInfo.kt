package com.homelib.book2
import com.google.gson.annotations.SerializedName

data class VolumeInfo (

	@SerializedName("title") val title : String,
	@SerializedName("subtitle") val subtitle : String,
	@SerializedName("authors") val authors : List<String>,
	@SerializedName("publisher") val publisher : String,
	@SerializedName("publishedDate") val publishedDate : String,
	@SerializedName("description") val description : String,
	@SerializedName("industryIdentifiers") val industryIdentifiers : List<IndustryIdentifiers>,
	@SerializedName("pageCount") val pageCount : Int,
	@SerializedName("categories") val categories : List<String>,
	@SerializedName("averageRating") val averageRating : Double,
	@SerializedName("ratingsCount") val ratingsCount : Int,
	@SerializedName("imageLinks") val imageLinks : ImageLinks,
	@SerializedName("language") val language : String,
	@SerializedName("infoLink") val infoLink : String
)