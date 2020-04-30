package com.homelib.bookTemplateOpenLibrary
import com.google.gson.annotations.SerializedName



data class BookDetails (

	@SerializedName("info_url") val info_url : String,
	@SerializedName("bib_key") val bib_bib_key : String,
	@SerializedName("preview_url") val preview_url : String,
	@SerializedName("thumbnail_url") val thumbnail_url : String,
	@SerializedName("details") val details : Details,
	@SerializedName("preview") val preview : String
)