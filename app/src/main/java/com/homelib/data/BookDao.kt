package com.homelib.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<Book>

    @Query("SELECT * FROM books WHERE book_id IN (:booksIds)")
    fun loadAllByIds(booksIds: IntArray) : List<Book>

    @Query("SELECT * FROM books WHERE author LIKE :author AND " + "title LIKE :title LIMIT 1")
    fun findByName(author: String, title: String): Book

    @Query("SELECT * FROM books WHERE isbn = :isbn ")
    fun getBookByIsbn(isbn: Long) : Book

    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE isbn = :isbn LIMIT 1)")
    suspend fun isBookExisting(isbn: Long) : Boolean

    @Insert
    fun insertAll(vararg users: Book)

    @Delete
    fun delete(user: Book)

    @Query("SELECT * from books ORDER BY title ASC")
    fun getAlphabetizedBooks(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) //if there is an existing records this prevents  duplication from happening
    suspend fun insert(book: Book) : Long

    @Query("DELETE FROM BOOKS")
    suspend fun deleteAll()
}