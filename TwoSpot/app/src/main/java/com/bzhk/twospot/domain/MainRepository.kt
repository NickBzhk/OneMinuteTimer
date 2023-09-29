package com.bzhk.twospot.domain

import com.bzhk.twospot.entity.MapObjects

interface MainRepository {
    suspend fun getMapObjectsList(
        categories: String,
        filter: String,
        bias: String
    ) : MapObjects?
}