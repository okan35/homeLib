package com.homelib.api

import com.homelib.data.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes?q=isbn")
    suspend fun getBooks(@Query(value = ":") isbn: String): Book
}