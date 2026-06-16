package com.example.syllabuzz.ui.progress

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
import com.example.syllabuzz.databinding.FragmentProgressApiBinding
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressApiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProgress()
    }

    override fun onResume() {
        super.onResume()
        val authManager = AuthManager.getInstance(requireContext())
        if (authManager.isLoggedIn() && binding.progressRecyclerView.visibility != View.VISIBLE) {
            loadProgress()
        }
    }

    private fun loadProgress() {
        val authManager = AuthManager.getInstance(requireContext())

        if (!authManager.isLoggedIn()) {
            binding.progressRecyclerView.visibility = View.GONE
            binding.loadingProgress.visibility = View.GONE
            binding.statusText.text = "Sign in to see your quiz progress"
            binding.btnLogin.visibility = View.VISIBLE
            binding.statusContainer.visibility = View.VISIBLE
            binding.btnLogin.setOnClickListener {
                findNavController().navigate(R.id.nav_login)
            }
            return
        }

        binding.statusContainer.visibility = View.GONE
        binding.progressRecyclerView.visibility = View.GONE
        binding.loadingProgress.visibility = View.VISIBLE

        val repo = MyQuizaRepository(authManager)
        lifecycleScope.launch {
            val result = repo.getMyProgress()
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { entries ->
                    if (entries.isEmpty()) {
                        binding.statusText.text = "No quiz attempts yet.\nStart a quiz to track your progress!"
                        binding.btnLogin.visibility = View.GONE
                        binding.statusContainer.visibility = View.VISIBLE
                    } else {
                        binding.progressRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.progressRecyclerView.adapter = ProgressAdapter(entries)
                        binding.progressRecyclerView.visibility = View.VISIBLE
                    }
                },
                onFailure = { error ->
                    if (error.message == "not_authenticated") {
                        binding.statusText.text = "Session expired. Please sign in again."
                        binding.btnLogin.visibility = View.VISIBLE
                    } else {
                        binding.statusText.text = "Failed to load progress. Please try again."
                        binding.btnLogin.visibility = View.GONE
                    }
                    binding.statusContainer.visibility = View.VISIBLE
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
