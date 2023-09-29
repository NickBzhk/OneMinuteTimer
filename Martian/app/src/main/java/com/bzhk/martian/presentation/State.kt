package com.bzhk.martian.presentation

sealed class State {
    object Loading : State()
    object Success :  State()
}
