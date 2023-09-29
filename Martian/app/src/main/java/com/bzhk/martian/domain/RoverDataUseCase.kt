package com.bzhk.martian.domain

import com.bzhk.martian.data.MainRepositoryImpl
import com.bzhk.martian.entity.RoverData

class RoverDataUseCase(
    private val mainRepositoryImpl: MainRepositoryImpl
) {
    suspend fun getDataFromCuriosity(earthDate: String, camera: String = "navcam"): RoverData {
        return mainRepositoryImpl.getDataFromCuriosity(earthDate, camera)
    }

    suspend fun getDataOpportunity(earthDate: String, camera: String = "pancam"): RoverData {
        return mainRepositoryImpl.getDataFromOpportunity(earthDate, camera)
    }

    suspend fun getDataFromSpirit(earthDate: String, camera: String = "pancam"): RoverData {
        return mainRepositoryImpl.getDataFromSpirit(earthDate, camera)
    }
}