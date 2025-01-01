package com.example.syllabuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.data.LessonData
import com.example.syllabuzz.databinding.FragmentLessonsBinding

class LessonsFragment : Fragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subjectName = arguments?.getString("subjectName") ?: "Unknown Subject"
        binding.subjectNameTextView.text = subjectName

        // Fetch lessons for the selected subject
        val lessons = LessonData.getLessons(subjectName)

        // Create a list of lesson names for display
        val lessonNames = lessons.map { it.first }

        val lessonsAdapter = LessonsAdapter(requireContext(), lessonNames, lessons)
        binding.lessonsRecyclerView.adapter = lessonsAdapter
        binding.lessonsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
