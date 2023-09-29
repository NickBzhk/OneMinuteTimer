package com.bzhk.martian.presentation.screen

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import com.bzhk.martian.R
import com.bzhk.martian.databinding.FragmentMenuBinding
import com.bzhk.martian.presentation.MainViewModel
import com.bzhk.martian.presentation.Rover
import com.bzhk.martian.presentation.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater)
        assignWidgets()
        return binding.root
    }

    private fun assignWidgets() {
        lateinit var bundle: Bundle
        binding.menuItemCuriosity.setOnClickListener {
            bundle = Bundle().apply {
                putSerializable("rover",
                    Rover.CURIOSITY)
            }
            findNavController()
                .navigate(R.id.action_menuFragment_to_mainFragment,
                    bundle)
        }

        binding.menuItemSpirit.setOnClickListener {
            bundle = Bundle().apply {
                putSerializable("rover",
                    Rover.SPIRIT)
            }
            findNavController()
                .navigate(R.id.action_menuFragment_to_mainFragment,
                    bundle)
        }

        binding.menuItemOpportunity.setOnClickListener {
            bundle = Bundle().apply {
                putSerializable("rover",
                    Rover.OPPORTUNITY)
            }
            findNavController()
                .navigate(R.id.action_menuFragment_to_mainFragment,
                    bundle)
        }

        binding.buttonInfoCuriosity.setOnClickListener {
            requestInfoDialog(
                Rover.CURIOSITY)
        }

        binding.buttonInfoSpirit.setOnClickListener {
            requestInfoDialog(
                Rover.SPIRIT)
        }

        binding.buttonInfoOpportunity.setOnClickListener {
            requestInfoDialog(
                Rover.OPPORTUNITY)
        }
    }

    private fun requestInfoDialog(rover: Rover) {
        viewModel.setRoverToObserve(rover)
        viewModel.makeCall()

        viewLifecycleOwner.lifecycleScope
            .launch(Dispatchers.Main) {
            withStarted { }
            viewModel.getState().collect { state ->

                when (state) {
                    State.Loading -> {
                        showProgressBar()
                    }

                    State.Success -> {
                        hideProgressBar()
                        showUnitInfoDialog(
                            unitInfoMessageBody())
                        this.coroutineContext
                            .job
                            .cancel()
                    }
                }
            }
        }
    }

    private fun unitInfoMessageBody(): String {
        var info = ""
        viewModel.getDataFromRover()
            .observe(
                viewLifecycleOwner) {
            info = """
                |Name: ${it.photos.first().rover.name}
                |Status: ${it.photos.first().rover.status}
                |Launched: ${it.photos.first().rover.launchDate}
                |Landing date: ${it.photos.first().rover.landingDate}
                |Max sols: ${it.photos.first().rover.maxSol}
            """.trimMargin("|")
        }
        return info
    }

    private fun showUnitInfoDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Unit info")
            .setMessage(message)
            .setNeutralButton("Close",
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface
                        .cancel()
                })
            .create()
            .show()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}