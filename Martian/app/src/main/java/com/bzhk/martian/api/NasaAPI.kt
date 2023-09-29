package com.bzhk.martian.api

import com.bzhk.martian.data.RoverDataDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getResponseFromCuriosity(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String,
        @Query("api_key") key: String = API_KEY
    ): Response<RoverDataDTO>

    @GET("mars-photos/api/v1/rovers/spirit/photos")
    suspend fun getResponseFromSpirit(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String,
        @Query("api_key") key: String = API_KEY
    ): Response<RoverDataDTO>

    @GET("mars-photos/api/v1/rovers/opportunity/photos")
    suspend fun getResponseFromOpportunity(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String,
        @Query("api_key") key: String = API_KEY
    ): Response<RoverDataDTO>

    private companion object {
        private const val API_KEY = "XGdfKNa7HF7Iwoss6OXLiTyX4Xe82ia1VMdwDzN9"
        private const val BASE_URL = "https://api.nasa.gov/"
    }
}