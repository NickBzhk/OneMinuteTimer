package com.bzhk.twospot.di

import com.bzhk.twospot.data.DataSource
import com.bzhk.twospot.data.MainRepositoryImpl
import com.bzhk.twospot.data.api.GeocodingAPI
import com.bzhk.twospot.domain.GetMapObjectsUseCase
import com.bzhk.twospot.domain.MainRepository
import com.bzhk.twospot.presentation.MapObjectsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.geoapify.com/"

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingAPI::class.java)
    }
    single {
        DataSource(get())
    }
    single<MainRepository> {
        MainRepositoryImpl(get())
    }
    factory {
        GetMapObjectsUseCase(get())
    }
    viewModel {
        MapObjectsViewModel(get())
    }
}