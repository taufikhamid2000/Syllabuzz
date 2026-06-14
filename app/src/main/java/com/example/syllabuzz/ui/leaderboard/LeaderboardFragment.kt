package com.example.syllabuzz.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.databinding.FragmentLeaderboardBinding
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var repo: MyQuizaRepository
    private var currentPeriod = "weekly"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = MyQuizaRepository(AuthManager.getInstance(requireContext()))

        binding.btnWeekly.setOnClickListener {
            currentPeriod = "weekly"
            loadLeaderboard()
        }
        binding.btnAllTime.setOnClickListener {
            currentPeriod = "all"
            loadLeaderboard()
        }

        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        binding.recyclerView.visibility = View.GONE
        binding.statusText.visibility = View.GONE
        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getLeaderboard(period = currentPeriod)
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { entries ->
                    if (entries.isEmpty()) {
                        binding.statusText.text = "No entries yet"
                        binding.statusText.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                        binding.recyclerView.adapter = LeaderboardAdapter(entries)
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                },
                onFailure = {
                    binding.statusText.text = "Failed to load leaderboard. Please try again."
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
