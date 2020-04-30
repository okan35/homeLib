package com.homelib.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder{

    private const val BASE_URL_GOOGLE_API = "https://www.googleapis.com/books/v1/"
    private const val BASE_URL_OPEN_LIBRARY = "https://openlibrary.org/api/"

    private fun getRetrofitGoogleApi(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_GOOGLE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun getRetrofitOpenLibrary(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_OPEN_LIBRARY)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


        val apiServiceGoogle: ApiService = getRetrofitGoogleApi().create(ApiService::class.java)

        val apiServiceOpenLibrary: ApiService = getRetrofitOpenLibrary().create(ApiService::class.java)


}