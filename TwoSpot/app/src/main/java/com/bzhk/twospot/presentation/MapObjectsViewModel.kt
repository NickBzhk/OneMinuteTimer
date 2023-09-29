package com.bzhk.twospot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzhk.twospot.domain.GetMapObjectsUseCase
import com.bzhk.twospot.entity.MapObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MapObjectsViewModel(
    private val getMapObjectsUseCase: GetMapObjectsUseCase
) : ViewModel() {

    suspend fun getMapObjects(
        categories: String,
        filter: String,
        bias: String
    ): Flow<MapObjects?> {
        return flowOf(getMapObjectsUseCase(
                categories,
                filter,
                bias
            ))
    }
}