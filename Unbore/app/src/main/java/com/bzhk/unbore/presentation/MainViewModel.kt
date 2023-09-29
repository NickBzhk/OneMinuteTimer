package com.bzhk.unbore.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzhk.unbore.domain.GetIdeaUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getIdeaUseCase: GetIdeaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private var activity: String = ""
    private var type: String = ""
    private var participants = 0
    private var price = 0.0
    private var query: List<String?> = listOfNotNull()

    fun makeRequest() {
        val scope = viewModelScope
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("Exception", "Caught exception: $throwable")
        }

        scope.coroutineContext.job.cancelChildren()
        scope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _state.value = State.Loading

            val idea = getIdeaUseCase.getIdea(query)

            activity = idea.activity
            type = idea.type
            participants = idea.participants
            price = idea.price

            _state.value = State.Success
        }
    }

    fun setQuery(list: List<String?>) {
        query = list
    }

    fun getQuery(): List<String?> {
        return query
    }

    fun getIdea(): String {
        return activity
    }

    fun getIdeaType(): String {
        return type
    }

    fun getParticipants(): Int {
        return participants
    }

    fun getPrice(): Double {
        return price
    }
}