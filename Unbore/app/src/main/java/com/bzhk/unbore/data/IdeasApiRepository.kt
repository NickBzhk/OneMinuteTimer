package com.bzhk.unbore.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private const val BASE_URL_RANDOM_USER = "https://www.boredapi.com"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_RANDOM_USER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val searchApi: SearchAPI = retrofit.create(SearchAPI::class.java)
}

interface SearchAPI {
    @GET("/api/activity/")
    suspend fun getResponseList(@Query("type") types: List<String?>): Response<IdeasDto>
}