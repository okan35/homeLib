package com.homelib.book2
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class VolumeInfo (

	@SerializedName("title") val title : String,
	@SerializedName("subtitle") val subtitle : String,
	@SerializedName("authors") val authors : List<String>,
	@SerializedName("publisher") val publisher : String,
	@SerializedName("publishedDate") val publishedDate : String,
	@SerializedName("description") val description : String,
	@SerializedName("industryIdentifiers") val industryIdentifiers : List<IndustryIdentifiers>,
	@SerializedName("readingModes") val readingModes : ReadingModes,
	@SerializedName("pageCount") val pageCount : Int,
	@SerializedName("printType") val printType : String,
	@SerializedName("categories") val categories : List<String>,
	@SerializedName("averageRating") val averageRating : Double,
	@SerializedName("ratingsCount") val ratingsCount : Int,
	@SerializedName("maturityRating") val maturityRating : String,
	@SerializedName("allowAnonLogging") val allowAnonLogging : Boolean,
	@SerializedName("contentVersion") val contentVersion : String,
	@SerializedName("panelizationSummary") val panelizationSummary : PanelizationSummary,
	@SerializedName("imageLinks") val imageLinks : ImageLinks,
	@SerializedName("language") val language : String,
	@SerializedName("previewLink") val previewLink : String,
	@SerializedName("infoLink") val infoLink : String,
	@SerializedName("canonicalVolumeLink") val canonicalVolumeLink : String
)