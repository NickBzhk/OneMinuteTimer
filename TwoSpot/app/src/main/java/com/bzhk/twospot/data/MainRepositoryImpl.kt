package com.bzhk.twospot.data

import com.bzhk.twospot.domain.MainRepository
import com.bzhk.twospot.entity.MapObjects

class MainRepositoryImpl(
    private val dataSource: DataSource
) : MainRepository {

    override suspend fun getMapObjectsList(
        categories: String,
        filter: String,
        bias: String
    ): MapObjects? {
            return dataSource.getMapObjects(
                categories,
                filter,
                bias
            )
    }
}