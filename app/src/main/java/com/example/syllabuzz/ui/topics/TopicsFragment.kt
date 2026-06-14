package com.example.syllabuzz.ui.topics

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

class TopicsFragment : Fragment() {

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

        val chapterId = arguments?.getString("chapterId") ?: return
        val chapterName = arguments?.getString("chapterName") ?: "Topics"
        requireActivity().title = chapterName

        val repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))
        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getTopics(chapterId)
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { topics ->
                    if (topics.isEmpty()) {
                        binding.emptyText.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                        binding.recyclerView.adapter = TopicsAdapter(topics) { topic ->
                            val bundle = Bundle().apply {
                                putString("topicId", topic.id)
                                putString("topicName", topic.name)
                            }
                            findNavController().navigate(R.id.action_topicsFragment_to_quizListFragment, bundle)
                        }
                    }
                },
                onFailure = {
                    binding.statusText.text = "Failed to load topics. Please try again."
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
