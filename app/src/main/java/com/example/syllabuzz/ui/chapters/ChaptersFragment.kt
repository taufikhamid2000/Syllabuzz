package com.example.syllabuzz.ui.chapters

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

class ChaptersFragment : Fragment() {

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

        val subjectId = arguments?.getString("subjectId") ?: return
        val subjectName = arguments?.getString("subjectName") ?: "Chapters"
        requireActivity().title = subjectName

        val repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))
        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getChapters(subjectId)
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { chapters ->
                    if (chapters.isEmpty()) {
                        binding.emptyText.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                        binding.recyclerView.adapter = ChaptersAdapter(chapters) { chapter ->
                            val bundle = Bundle().apply {
                                putString("chapterId", chapter.id)
                                putString("chapterName", chapter.name)
                            }
                            findNavController().navigate(R.id.action_chaptersFragment_to_topicsFragment, bundle)
                        }
                    }
                },
                onFailure = {
                    binding.statusText.text = "Failed to load chapters. Please try again."
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
