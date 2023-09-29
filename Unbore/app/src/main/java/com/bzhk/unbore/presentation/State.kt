package com.bzhk.unbore.presentation

sealed class State() {
    object Loading : State()
    object Success : State()
    object Error : State()
}
