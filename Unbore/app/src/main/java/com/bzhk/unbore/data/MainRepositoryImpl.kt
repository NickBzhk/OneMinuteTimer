package com.bzhk.unbore.data

import com.bzhk.unbore.domain.MainRepository
import com.bzhk.unbore.entity.IIdea
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val ideasDataSource: IdeasDataSource
) : MainRepository {
    override suspend fun getIdea(query: List<String?>): IIdea {
        return ideasDataSource.loadIdea(query)
    }
}
