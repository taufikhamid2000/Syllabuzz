package com.example.syllabuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.databinding.FragmentSubjectsBinding
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
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

        val repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))

        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getSubjects()
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { subjects ->
                    val active = subjects.filter { !it.isDisabled }
                    if (active.isEmpty()) {
                        binding.statusText.text = "No subjects available"
                        binding.statusText.visibility = View.VISIBLE
                    } else {
                        binding.subjectsRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.subjectsRecyclerView.adapter = SubjectsAdapter(active) { subject ->
                            val bundle = Bundle().apply {
                                putString("subjectId", subject.id)
                                putString("subjectName", subject.name)
                            }
                            findNavController().navigate(R.id.action_subjectsFragment_to_chaptersFragment, bundle)
                        }
                        binding.subjectsRecyclerView.visibility = View.VISIBLE
                    }
                },
                onFailure = {
                    binding.statusText.text = "Could not load subjects.\nCheck your connection and try again."
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
