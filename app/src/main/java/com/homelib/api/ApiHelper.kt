package com.homelib.api

class ApiHelper (private val apiService: ApiService) {

    suspend fun getBooksFromGoogle(isbn: String) = apiService.getBooksFromGoogleApi(isbn)

    suspend fun getBooksFromOpenLibrary(isbn: String) = apiService.getBooksFromOpenLibraryApiMap(isbn)

}