package com.example.syllabuzz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.syllabuzz.R
import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.databinding.FragmentHomeBinding
import com.example.syllabuzz.network.model.QuizSummary
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

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
            launchRandomQuiz()
        }
    }

    /**
     * No "random quiz" endpoint exists, so walk the tree randomly:
     * subject → chapter → topic → quizzes, keeping only quizzes that have
     * questions. Retry from a fresh path if a topic turns up empty.
     */
    private fun launchRandomQuiz() {
        val repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))
        val originalText = binding.btnExtra.text
        binding.btnExtra.isEnabled = false
        binding.btnExtra.text = "Finding a quiz…"

        viewLifecycleOwner.lifecycleScope.launch {
            val quiz = findRandomQuiz(repo)

            // Fragment may be gone if the user navigated away during a cold start
            if (_binding == null) return@launch
            binding.btnExtra.isEnabled = true
            binding.btnExtra.text = originalText

            if (quiz == null) {
                Toast.makeText(
                    requireContext(),
                    "Couldn't find a quiz with questions. Tap again to retry.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val bundle = Bundle().apply {
                    putString("quizId", quiz.id)
                    putString("quizName", quiz.name)
                }
                findNavController().navigate(R.id.action_nav_home_to_quizFragment, bundle)
            }
        }
    }

    /**
     * Shuffled depth-first scan with early exit. Order is randomized so the
     * pick varies, but unlike pure random sampling this keeps walking until it
     * actually finds a populated quiz — essential when content is sparse.
     * Capped at [MAX_API_CALLS] so a huge tree can't spin forever (dense trees
     * hit a quiz almost immediately and exit well before the cap).
     */
    private suspend fun findRandomQuiz(repo: MyQuizaRepository): QuizSummary? {
        val subjects = repo.getSubjects().getOrNull()
            ?.filter { !it.isDisabled }
            ?.shuffled()
            ?: return null

        var calls = 1 // getSubjects
        for (subject in subjects) {
            if (calls >= MAX_API_CALLS) return null
            val chapters = repo.getChapters(subject.id).getOrNull().orEmpty().shuffled()
            calls++
            for (chapter in chapters) {
                if (calls >= MAX_API_CALLS) return null
                val topics = repo.getTopics(chapter.id).getOrNull().orEmpty().shuffled()
                calls++
                for (topic in topics) {
                    if (calls >= MAX_API_CALLS) return null
                    val quizzes = repo.getQuizzes(topic.id).getOrNull().orEmpty()
                        .filter { it.questionCount > 0 }
                    calls++
                    if (quizzes.isNotEmpty()) return quizzes.random()
                }
            }
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Safety ceiling so a very large content tree can't scan indefinitely.
        private const val MAX_API_CALLS = 200
    }
}
