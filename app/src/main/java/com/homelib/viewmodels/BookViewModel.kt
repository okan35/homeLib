package com.homelib.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.homelib.data.Book
import com.homelib.data.BookDatabase
import com.homelib.data.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: LiveData<List<Book>>
    var isBookSaved : Long = 0


    init {
        val usersDao = BookDatabase.getDatabase(
            application,
            viewModelScope
        ).bookDao()
        repository = BookRepository(usersDao)
        allUsers = repository.allUsers
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(book: Book) = viewModelScope.launch(Dispatchers.IO) {
       isBookSaved = repository.insert(book)
    }

    fun getBookByIsbn(isbn: Long) = repository.getBookByIsbn(isbn)

    suspend fun isBookExisting(isbn: Long) = repository.isBookExisting(isbn)

}