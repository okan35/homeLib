package com.homelib.api


import com.homelib.book2.BookBase
import com.homelib.book2.Items
import com.homelib.data.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes")
    suspend fun getBooks(@Query(value = "q") isbn: String): BookBase
}