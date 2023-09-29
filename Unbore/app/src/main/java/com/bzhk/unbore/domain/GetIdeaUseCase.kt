package com.bzhk.unbore.domain

import com.bzhk.unbore.data.LocalStorageRepository
import com.bzhk.unbore.data.MainRepositoryImpl
import com.bzhk.unbore.entity.IIdea
import javax.inject.Inject

class GetIdeaUseCase @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl,
    private val localStorageRepository: LocalStorageRepository
) {

    suspend fun getIdea(query: List<String?>): IIdea {
        val idea = mainRepositoryImpl.getIdea(query)
        localStorageRepository.save(idea)
        return mainRepositoryImpl.getIdea(query)
    }
}