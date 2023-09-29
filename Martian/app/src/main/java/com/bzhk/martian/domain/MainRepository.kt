package com.bzhk.martian.domain

import com.bzhk.martian.entity.RoverData

interface MainRepository {
    suspend fun getDataFromCuriosity(earthDate: String, camera: String): RoverData
    suspend fun getDataFromOpportunity(earthDate: String, camera: String): RoverData
    suspend fun getDataFromSpirit(earthDate: String, camera: String): RoverData
}