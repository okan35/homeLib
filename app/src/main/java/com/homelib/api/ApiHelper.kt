package com.homelib.api

class ApiHelper (private val apiService: ApiService) {

    suspend fun getBooks(isbn: String) = apiService.getBooks(isbn)

}