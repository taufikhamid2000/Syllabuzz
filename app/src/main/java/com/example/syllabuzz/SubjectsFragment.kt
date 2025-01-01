package com.example.syllabuzz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.databinding.FragmentSubjectsBinding

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Fetch and display the list of SPM subjects
        Log.d("SubjectsFragment", "onViewCreated called")
        val subjects = listOf("Bahasa Melayu", "English", "Mathematics", "Science", "History")
        val subjectsAdapter = SubjectsAdapter(subjects)
        binding.subjectsRecyclerView.adapter = subjectsAdapter
        binding.subjectsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}