package com.bzhk.unbore.di

import com.bzhk.unbore.presentation.MainViewModelFactory
import dagger.Component

@Component
interface AppComponent {
    fun mainViewModelFactory(): MainViewModelFactory
}