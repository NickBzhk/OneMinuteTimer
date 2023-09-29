package com.bzhk.twospot.data

import com.bzhk.twospot.data.api.GeocodingAPI
import com.bzhk.twospot.data.dto.MapObjectsDTO
import com.bzhk.twospot.entity.MapObjects
import retrofit2.Response

class DataSource(
    private val geocodingAPI: GeocodingAPI
) {
    suspend fun getMapObjects(
        categories: String,
        filter: String,
        bias: String
    ): MapObjects? {
        return geocodingAPI.getResponseForRadius(
            categories,
            filter,
            bias
        ).body()
    }
}