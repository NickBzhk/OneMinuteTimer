package com.bzhk.martian.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import com.bzhk.martian.R
import com.bzhk.martian.data.Photo
import com.bzhk.martian.databinding.FragmentPhotoGridBinding
import com.bzhk.martian.presentation.MainViewModel
import com.bzhk.martian.presentation.Rover
import com.bzhk.martian.presentation.State
import com.bzhk.martian.presentation.recycler_view.RoverListAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZoneId
import java.util.Calendar

class PhotoGridFragment : Fragment() {
    private companion object {
        private const val SELECTED_ROVER = "rover"
        private const val EARTH_DAY_CYCLE = 86400000L
    }

    private val listAdapter = RoverListAdapter { (onItemClick(it)) }
    private val viewModel by viewModel<MainViewModel>()
    private var _binding: FragmentPhotoGridBinding? = null
    private var selectedRover: Rover = Rover.CURIOSITY

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedRover = it.get(SELECTED_ROVER) as Rover
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGridBinding.inflate(layoutInflater)
        observeState()
        observeRoverData()
        return binding.root
    }

    private fun observeRoverData() {
        viewModel.setRoverToObserve(selectedRover)
        viewModel.makeCall()

        viewModel.getDataFromRover()
            .observe(viewLifecycleOwner) { data ->
                listAdapter.submitList(data.photos)
                when (data.photos.firstOrNull()?.imgSrc?.isNotEmpty()) {
                    true -> hideNoDataMessage()
                    else -> showNoDataMessage()
                }
            }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope
            .launch {
                withStarted { }
                viewModel.getState().collect { state ->

                    when (state) {
                        State.Loading -> {
                            caseShownProgressBar()
                        }

                        State.Success -> {
                            caseHiddenProgressBar()
                            assignAdapter()
                            createCalendar()
                        }
                    }
                }
            }
    }

    private fun assignAdapter() {
        binding.recycler.adapter = listAdapter
    }

    private suspend fun createCalendar() {
        val constraintStartCalendar =
            Calendar.getInstance()
        val constraintEndCalendar =
            Calendar.getInstance()

        val missionStartDate = lifecycleScope.async {
            viewModel.getRoverMinDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }.await()

        val missionMaxDate = lifecycleScope.async {
            viewModel.getRoverMaxDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }.await()

        constraintStartCalendar.timeInMillis =
            missionStartDate + EARTH_DAY_CYCLE
        constraintEndCalendar.timeInMillis =
            missionMaxDate - EARTH_DAY_CYCLE

        val calendarConstrains =
            CalendarConstraints.Builder()
                .setValidator(
                    CompositeDateValidator.allOf(
                        listOf(
                            DateValidatorPointForward
                                .from(constraintStartCalendar.timeInMillis),
                            DateValidatorPointBackward
                                .before(constraintEndCalendar.timeInMillis)
                        )
                    )
                )
                .setStart(constraintStartCalendar.timeInMillis)
                .setEnd(constraintEndCalendar.timeInMillis)
                .build()

        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateDialog = MaterialDatePicker
                .Builder
                .datePicker()
                .setCalendarConstraints(calendarConstrains)
                .build()

            dateDialog.addOnPositiveButtonClickListener { timeInMillis ->
                calendar.timeInMillis = timeInMillis
                viewModel.setDateToObserve(
                    calendar.time
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
                viewModel.makeCall()
            }

            dateDialog.show(childFragmentManager, "DatePicker")

        }

        binding.datePickerNext.setOnClickListener {
            nextDay()
        }

        binding.datePickerPrev.setOnClickListener {
            prevDay()
        }
    }

    private fun nextDay() {
        viewModel.setDateToObserve(
            viewModel.getSelectedDate().plusDays(1)
        )
        viewModel.makeCall()
    }

    private fun prevDay() {
        viewModel.setDateToObserve(
            viewModel.getSelectedDate().minusDays(1)
        )
        viewModel.makeCall()
    }

    private fun onItemClick(photo: Photo) {
        val bundle: Bundle =
            Bundle().apply {
                putString("imgSrc", photo.imgSrc)
            }
        findNavController()
            .navigate(R.id.action_mainFragment_to_photoFragment, bundle)
    }

    private fun caseHiddenProgressBar() {
        with(binding) {
            progressBar.visibility = View.GONE

            datePickerNext.visibility = View.VISIBLE
            datePickerPrev.visibility = View.VISIBLE
            datePicker.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun caseShownProgressBar() {
        with(binding) {
            progressBar.visibility = View.VISIBLE

            datePicker.visibility = View.GONE
            datePickerNext.visibility = View.GONE
            datePickerPrev.visibility = View.GONE
            recycler.visibility = View.GONE
            noDataSign.visibility = View.GONE
        }
    }

    private fun showNoDataMessage() {
        binding.noDataSign.visibility = View.VISIBLE
    }

    private fun hideNoDataMessage() {
        binding.noDataSign.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}