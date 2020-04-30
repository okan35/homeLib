package com.homelib.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.homelib.api.ApiHelper
import com.homelib.api.RetrofitBuilder
import com.homelib.bookTemplateOpenLibrary.BookDetails
import com.homelib.data.Book
import com.homelib.data.BookDatabase
import com.homelib.repository.BookRepository
import com.homelib.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: BookRepository
    private var apiHelper: ApiHelper
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    var allUsers: LiveData<List<Book>>;
    var isBookSaved : Long = 0


    init {
        val usersDao = BookDatabase.getDatabase(
            application,
            viewModelScope
        ).bookDao()

        apiHelper = ApiHelper(RetrofitBuilder.apiServiceGoogle)
        repository = BookRepository(usersDao, apiHelper)
        allUsers = repository.allUsers
    }

    private fun initGoogleApi(){
        val usersDao = BookDatabase.getDatabase(
            getApplication(),
            viewModelScope
        ).bookDao()

        apiHelper = ApiHelper(RetrofitBuilder.apiServiceGoogle)
        repository = BookRepository(usersDao, apiHelper)
        allUsers = repository.allUsers
    }

    private fun initOpenLibraryApi(){
        val usersDao = BookDatabase.getDatabase(
            getApplication(),
            viewModelScope
        ).bookDao()

        apiHelper = ApiHelper(RetrofitBuilder.apiServiceOpenLibrary)
        repository = BookRepository(usersDao, apiHelper)
        allUsers = repository.allUsers
    }

    fun getBookFromGoogle(isbn: String) = liveData(Dispatchers.IO) {
        initGoogleApi()
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getBookFromGoogle(isbn)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getBookFromOpenLibrary(isbn: String) :LiveData<Resource<HashMap<String, BookDetails>>> = liveData(Dispatchers.IO) {
        initOpenLibraryApi()
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getBookFromOpenLibrary(isbn)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
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