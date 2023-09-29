package com.bzhk.martian.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.bzhk.martian.databinding.FragmentPhotoViewBinding

class PhotoViewFragment : Fragment() {

    private companion object{
        private const val IMG_SRC = "imgSrc"
        private const val DESCRIPTION = "description"
    }
    private var selectedImg = ""
    private var description = ""

    private var _binding: FragmentPhotoViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedImg = it.getString(IMG_SRC).toString()
            description = it.getString(DESCRIPTION).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoViewBinding.inflate(layoutInflater)
        binding.photo.load(selectedImg)
        binding.root.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}