package com.homelib.api
import com.homelib.bookTemplateGoogle.BookBase
import com.homelib.bookTemplateOpenLibrary.BookDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes")
    suspend fun getBooksFromGoogleApi(@Query(value = "q") isbn: String): BookBase

    @GET("https://openlibrary.org/api/books?jscmd=details&format=json")
    suspend fun getBooksFromOpenLibraryApiMap(@Query("bibkeys") bibkeys: String): HashMap<String, BookDetails>

}