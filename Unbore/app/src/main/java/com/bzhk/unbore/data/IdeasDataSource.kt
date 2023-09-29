package com.bzhk.unbore.data

import javax.inject.Inject

class IdeasDataSource @Inject constructor() {
    suspend fun loadIdea(query: List<String?>): IdeasDto {

        val response = RetrofitInstance.searchApi.getResponseList(query)

        return IdeasDto(
            activity = response.body()?.activity ?: "",
            type = response.body()?.type ?: "",
            participants = response.body()?.participants ?: 0,
            price = response.body()?.price ?: 0.0,
        )
    }
}