package com.bzhk.unbore.domain

import com.bzhk.unbore.entity.IIdea

interface MainRepository {
    suspend fun getIdea(query: List<String?>): IIdea
}