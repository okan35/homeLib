package com.homelib.repository


import androidx.lifecycle.LiveData
import com.homelib.api.ApiHelper
import com.homelib.data.Book
import com.homelib.data.BookDao


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class BookRepository(private val bookDao: BookDao, private val apiHelper: ApiHelper) {



    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allUsers: LiveData<List<Book>> = bookDao.getAlphabetizedBooks()

    suspend fun insert(book: Book) : Long{
         return bookDao.insert(book)
    }

    fun getBookByIsbn(isbn: Long) : Book {
       return bookDao.getBookByIsbn(isbn)
    }

    suspend fun isBookExisting(isbn: Long) = bookDao.isBookExisting(isbn)

    suspend fun getBookFromGoogle(isbn: String) = apiHelper.getBooksFromGoogle(isbn)

    suspend fun getBookFromOpenLibrary(isbn: String) = apiHelper.getBooksFromOpenLibrary(isbn)



}