package com.bzhk.martian.data

import com.bzhk.martian.domain.MainRepository
import com.bzhk.martian.entity.RoverData

class MainRepositoryImpl(
    private val dataSource: RoverDataSource
) : MainRepository {
    override suspend fun getDataFromCuriosity(earthDate: String, camera: String): RoverData {
        return dataSource.loadDataFromCuriosity(earthDate, camera)
    }

    override suspend fun getDataFromOpportunity(earthDate: String, camera: String): RoverData {
        return dataSource.loadDataFromOpportunity(earthDate, camera)
    }

    override suspend fun getDataFromSpirit(earthDate: String, camera: String): RoverData {
        return dataSource.loadDataFromSpirit(earthDate, camera)
    }
}