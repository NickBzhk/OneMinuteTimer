package com.bzhk.twospot.domain

import com.bzhk.twospot.entity.MapObjects

class GetMapObjectsUseCase(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(
        categories: String,
        filter: String,
        bias: String
    ): MapObjects? {
        return mainRepository.getMapObjectsList(
            categories,
            filter,
            bias
        )
    }
}