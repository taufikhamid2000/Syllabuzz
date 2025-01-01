package com.example.syllabuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.syllabuzz.databinding.FragmentLessonContentBinding
import com.example.syllabuzz.utils.ProgressManager

class LessonContentFragment : Fragment() {

    private var _binding: FragmentLessonContentBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressManager: ProgressManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonContentBinding.inflate(inflater, container, false)
        progressManager = ProgressManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessonName = arguments?.getString("lessonName") ?: "Lesson Content"
        val lessonDetails = arguments?.getString("lessonDetails") ?: "Details not available."

        binding.lessonTitleTextView.text = lessonName
        binding.lessonContentTextView.text = lessonDetails

        val isCompleted = progressManager.isLessonCompleted(lessonName)
        binding.completeButton.text = if (isCompleted) "Mark as Incomplete" else "Mark as Complete"

        binding.completeButton.setOnClickListener {
            val newStatus = !progressManager.isLessonCompleted(lessonName)
            progressManager.setLessonCompleted(lessonName, newStatus)
            binding.completeButton.text = if (newStatus) "Mark as Incomplete" else "Mark as Complete"
            Toast.makeText(context, "Lesson updated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
