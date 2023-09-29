package com.bzhk.martian.data

import com.bzhk.martian.api.Api

class RoverDataSource(
    private val api: Api
) {

    suspend fun loadDataFromCuriosity(earthDate: String, camera: String): RoverDataDTO {
        val api = this.api.getResponseFromCuriosity(earthDate, camera)
        return RoverDataDTO(api.body()?.photos ?: listOf())
    }

    suspend fun loadDataFromOpportunity(earthDate: String, camera: String): RoverDataDTO {
        val api = this.api.getResponseFromOpportunity(earthDate, camera)
        return RoverDataDTO(api.body()?.photos ?: listOf())
    }

    suspend fun loadDataFromSpirit(earthDate: String, camera: String): RoverDataDTO {
        val api = this.api.getResponseFromSpirit(earthDate, camera)
        return RoverDataDTO(api.body()?.photos ?: listOf())
    }
}