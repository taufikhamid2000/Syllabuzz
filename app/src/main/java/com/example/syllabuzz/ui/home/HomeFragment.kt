package com.example.syllabuzz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.syllabuzz.R
import com.example.syllabuzz.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button Click Listeners
        binding.btnSubjects.setOnClickListener {
            findNavController().navigate(R.id.nav_subjects)
        }

        binding.btnProgress.setOnClickListener {
            findNavController().navigate(R.id.nav_progress)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.nav_settings)
        }

        binding.btnExtra.setOnClickListener {
            // Placeholder for additional functionality
            // Example: Navigate to an "About" or "Help" section
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
