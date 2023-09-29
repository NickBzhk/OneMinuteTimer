package com.bzhk.martian.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzhk.martian.domain.RoverDataUseCase
import com.bzhk.martian.entity.RoverData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(private val roverData: RoverDataUseCase) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    private val state = _state.asStateFlow()
    private val _livedata = MutableLiveData<RoverData>()
    private val liveData: LiveData<RoverData> = _livedata
    private var selectedRover: Rover = Rover.CURIOSITY

    fun getState(): StateFlow<State> = state

    fun setRoverToObserve(rover: Rover) { selectedRover = rover }

    fun getDataFromRover(): LiveData<RoverData> { return liveData }

    /** DATE SETTINGS **/
    private var selectedDate = when (selectedRover) {
        Rover.CURIOSITY ->
            LocalDate.parse(
                CURIOSITY_LANDING_DATE
            )

        Rover.SPIRIT ->
            LocalDate.parse(
                SPIRIT_LANDING_DATE
            )

        else ->
            LocalDate.parse(
                OPPORTUNITY_LANDING_DATE
            )
    }

    private var roverMinDate = when (selectedRover) {
        Rover.CURIOSITY ->
            LocalDate.parse(
                CURIOSITY_LANDING_DATE
            )

        Rover.SPIRIT ->
            LocalDate.parse(
                SPIRIT_LANDING_DATE
            )

        else ->
            LocalDate.parse(
                OPPORTUNITY_LANDING_DATE
            )
    }

    private var roverMaxDate = LocalDate.parse(CURIOSITY_LANDING_DATE)

    suspend fun getRoverMaxDate(): LocalDate = roverMaxDate

    fun getRoverMinDate(): LocalDate = roverMinDate

    fun getSelectedDate(): LocalDate = selectedDate

    fun setDateToObserve(time: LocalDate) {
        selectedDate = time
    }

    /** API CALL **/
    fun makeCall() {
        val scope = viewModelScope
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("Exception","Caught exception: $throwable")
        }

        scope.launch(Dispatchers.IO + coroutineExceptionHandler) {

                _state.value = State.Loading

                val formattedDate = selectedDate.format(
                    DateTimeFormatter
                        .ofPattern(US_DATE_FORMAT)
                )

                val api = when (selectedRover) {
                    Rover.CURIOSITY ->
                        roverData.getDataFromCuriosity(
                            earthDate = formattedDate
                        )

                    Rover.SPIRIT ->
                        roverData.getDataFromSpirit(
                            earthDate = formattedDate
                        )

                    else ->
                        roverData.getDataOpportunity(
                            earthDate = formattedDate
                        )
                }

                api.photos.forEach { Log.i(DATA, "$it\n") }

                roverMaxDate = LocalDate.parse(
                        api.photos
                            .firstOrNull()
                            ?.rover
                            ?.maxDate
                            ?: selectedDate.toString()
                    )

                roverMinDate = LocalDate.parse(
                        api.photos
                            .firstOrNull()
                            ?.rover
                            ?.landingDate
                            ?: selectedDate.toString()
                    )

                _livedata.postValue(api)

                _state.value = State.Success
            }
    }

    private companion object {
        const val DATA = "DATA"
        const val US_DATE_FORMAT = "yyyy-MM-dd"
        const val SPIRIT_LANDING_DATE = "2004-01-05"
        const val CURIOSITY_LANDING_DATE = "2012-08-07"
        const val OPPORTUNITY_LANDING_DATE = "2004-01-26"
    }
}