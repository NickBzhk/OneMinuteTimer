package com.bzhk.twospot.data.api

import com.bzhk.twospot.data.dto.MapObjectsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {
    companion object {
        const val OTP_URL_PATH_RADIUS = "/v2/places"
        const val API_KEY = "22ee0646ff054b079229cbdca2087692"
    }

    @GET(OTP_URL_PATH_RADIUS)
    suspend fun getResponseForRadius(
        @Query("categories") categories: String,
        @Query("filter") filter: String,
        @Query("bias") bias: String,
        @Query("limit") limit: Int = 14,
        @Query("apiKey") apikey: String = API_KEY
    ): Response<MapObjectsDTO>
}

/**
 * Example URL body:
 * https://api.geoapify.com/v2/
 * places?
 * categories=service.financial
 * &filter=circle:30.26086719086169,59.897464799999995,1500
 * &bias=proximity:30.26086719086169,59.897464799999995
 * &limit=20
 * &apiKey=[***]
 * */