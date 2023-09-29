package com.bzhk.martian.di

import com.bzhk.martian.api.Api
import com.bzhk.martian.data.MainRepositoryImpl
import com.bzhk.martian.data.RoverDataDTO
import com.bzhk.martian.data.RoverDataSource
import com.bzhk.martian.domain.RoverDataUseCase
import com.bzhk.martian.entity.RoverData
import com.bzhk.martian.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.nasa.gov/"

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
    single {
        RoverDataSource(get())
    }
    single {
        MainRepositoryImpl(get())
    }
    factory<RoverData> {
        RoverDataDTO(get())
    }
    factory {
        RoverDataUseCase(get())
    }
    viewModel {
        MainViewModel(get())
    }
}