package com.bzhk.unbore.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.bzhk.unbore.databinding.FragmentMainBinding
import com.bzhk.unbore.di.DaggerAppComponent
import com.bzhk.unbore.presentation.MainViewModel
import com.bzhk.unbore.presentation.State
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels {
        DaggerAppComponent.create().mainViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        assignWidgets()
        checkState()
        return binding.root
    }

    private fun checkState() {
        viewLifecycleOwner.lifecycleScope.launch {
            withStarted { }
            viewModel.state.collect { state ->
                when (state) {

                    State.Loading -> {
                        showProgressBar()
                        makeRequest()
                    }

                    State.Error -> {
                        makeRequest()
                        showProgressBar()
                    }

                    State.Success -> {
                        hideProgressBar()
                        assignFields()
                    }
                }
            }
        }
    }

    private fun refresh() {
        checkState()
        makeRequest()
    }

    private fun filteredQuery(): List<String?> {
        val query: MutableList<String?> = mutableListOf()
        if (binding.filterChipSocial.isChecked) query += "social"
        if (binding.filterChipBusywork.isChecked) query += "busywork"
        if (binding.filterChipEducational.isChecked) query += "education"
        if (binding.filterChipRecreational.isChecked) query += "recreational"
        return query
    }

    private fun assignFields() {
        binding.fieldIdeaSubject.text = viewModel.getIdea()
        binding.fieldIdeaType.text = viewModel.getIdeaType()
    }

    private fun assignWidgets() {
        binding.buttonNext.setOnClickListener {
            viewModel.setQuery(filteredQuery())
            refresh()
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.buttonNext.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonNext.visibility = View.GONE
    }

    private fun makeRequest() {
        viewModel.makeRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}