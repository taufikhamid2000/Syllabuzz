package com.example.syllabuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.data.LessonData
import com.example.syllabuzz.databinding.FragmentProgressBinding
import com.example.syllabuzz.utils.ProgressManager

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressManager: ProgressManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        progressManager = ProgressManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Fixed the issue by passing savedInstanceState

        val progressData = mutableListOf<Pair<String, Int>>()

        for ((subject, lessons) in LessonData.getAllLessons()) {
            val completedCount = lessons.count { progressManager.isLessonCompleted(it.first) }
            progressData.add(Pair(subject, completedCount))
        }

        val progressAdapter = ProgressAdapter(progressData)
        binding.progressRecyclerView.adapter = progressAdapter
        binding.progressRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
