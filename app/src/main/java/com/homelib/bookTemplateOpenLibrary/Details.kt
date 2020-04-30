package com.homelib.bookTemplateOpenLibrary
import com.google.gson.annotations.SerializedName

data class Details (

	@SerializedName("publishers") val publishers : List<String>,
	@SerializedName("physical_format") val physical_format : String,
	@SerializedName("source_records") val source_records : List<String>,
	@SerializedName("title") val title : String,
	@SerializedName("identifiers") val identifiers : Identifiers,
	@SerializedName("isbn_13") val isbn_13 : List<String>,
	@SerializedName("covers") val covers : List<Int>,
	@SerializedName("created") val created : Created,
	@SerializedName("languages") val languages : List<Languages>,
	@SerializedName("last_modified") val last_modified : Last_modified,
	@SerializedName("latest_revision") val latest_revision : Int,
	@SerializedName("publish_country") val publish_country : String,
	@SerializedName("key") val key : String,
	@SerializedName("authors") val authors : List<Authors>,
	@SerializedName("publish_date") val publish_date : Int,
	@SerializedName("publish_places") val publish_places : List<String>,
	@SerializedName("works") val works : List<Works>,
	@SerializedName("type") val type : Type,
	@SerializedName("revision") val revision : Int
)