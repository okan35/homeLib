package com.homelib.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "image_link") val imageLink: String?,
    @ColumnInfo(name = "isbn") val isbn: String?
) {
    @PrimaryKey(autoGenerate = true)
    val book_id: Int = 0
}
