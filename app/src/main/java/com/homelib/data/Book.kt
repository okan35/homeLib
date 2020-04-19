package com.homelib.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "year") var year: String?,
    @ColumnInfo(name = "image_link") var imageLink: String?,
    @ColumnInfo(name = "isbn") var isbn: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    var bookId: Int = 0
}
