package com.example.syllabuzz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.R
import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.databinding.FragmentListGenericBinding
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

class QuizListFragment : Fragment() {

    private var _binding: FragmentListGenericBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListGenericBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topicId = arguments?.getString("topicId") ?: return
        val topicName = arguments?.getString("topicName") ?: "Quizzes"
        requireActivity().title = topicName

        val repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))
        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getQuizzes(topicId)
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { quizzes ->
                    if (quizzes.isEmpty()) {
                        binding.emptyText.text = "No quizzes available for this topic yet"
                        binding.emptyText.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                        binding.recyclerView.adapter = QuizListAdapter(quizzes) { quiz ->
                            val bundle = Bundle().apply {
                                putString("quizId", quiz.id)
                                putString("quizName", quiz.name)
                            }
                            findNavController().navigate(R.id.action_quizListFragment_to_quizFragment, bundle)
                        }
                    }
                },
                onFailure = {
                    binding.statusText.text = "Failed to load quizzes. Please try again."
                    binding.statusText.visibility = View.VISIBLE
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
