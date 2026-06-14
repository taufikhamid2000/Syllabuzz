package com.example.syllabuzz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.syllabuzz.R
import com.example.syllabuzz.auth.AuthManager
import com.example.syllabuzz.databinding.FragmentQuizBinding
import com.example.syllabuzz.network.model.Answer
import com.example.syllabuzz.network.model.AttemptRequest
import com.example.syllabuzz.network.model.Question
import com.example.syllabuzz.repository.MyQuizaRepository
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val selectedAnswers = mutableMapOf<String, MutableList<String>>()
    private var startTimeMs = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizId = arguments?.getString("quizId") ?: return
        val quizName = arguments?.getString("quizName") ?: "Quiz"

        val authManager = AuthManager.getInstance(requireContext())
        val repo = MyQuizaRepository(authManager)

        if (!authManager.isLoggedIn()) {
            binding.statusText.text = "Please sign in to take quizzes"
            binding.statusText.visibility = View.VISIBLE
            return
        }

        binding.loadingProgress.visibility = View.VISIBLE
        binding.statusText.text = "Loading quiz…"
        binding.statusText.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.getQuizDetail(quizId)
            binding.loadingProgress.visibility = View.GONE

            result.fold(
                onSuccess = { quiz ->
                    binding.statusText.visibility = View.GONE
                    binding.quizTitle.text = quiz.name
                    startTimeMs = System.currentTimeMillis()

                    binding.questionsRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.questionsRecyclerView.adapter = QuestionsAdapter(quiz.questions, selectedAnswers)
                    binding.quizContent.visibility = View.VISIBLE

                    binding.btnSubmit.setOnClickListener {
                        submitQuiz(repo, quizId)
                    }
                },
                onFailure = {
                    binding.statusText.text = "Failed to load quiz. Please try again."
                    binding.statusText.visibility = View.VISIBLE
                }
            )
        }

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun submitQuiz(repo: MyQuizaRepository, quizId: String) {
        val timeTakenSeconds = ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
        val answers = selectedAnswers.map { (questionId, optionIds) ->
            Answer(questionId, optionIds.toList())
        }

        binding.btnSubmit.isEnabled = false
        binding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = repo.submitAttempt(quizId, AttemptRequest(answers, timeTakenSeconds))
            binding.loadingProgress.visibility = View.GONE
            binding.btnSubmit.isEnabled = true

            result.fold(
                onSuccess = { response ->
                    binding.quizContent.visibility = View.GONE
                    binding.resultScore.text = "${response.score}%"
                    binding.resultDetail.text = "${response.correctAnswers} / ${response.totalQuestions} correct"
                    binding.resultXp.text = if (response.xpAwarded) "+50 XP earned!" else ""
                    binding.resultCard.visibility = View.VISIBLE
                },
                onFailure = { error ->
                    val msg = if (error.message == "not_authenticated") {
                        "Please sign in to submit the quiz"
                    } else {
                        "Submission failed. Please try again."
                    }
                    binding.statusText.text = msg
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

private class QuestionsAdapter(
    private val questions: List<Question>,
    private val selectedAnswers: MutableMap<String, MutableList<String>>
) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val questionType: TextView = view.findViewById(R.id.questionType)
        val optionsContainer: LinearLayout = view.findViewById(R.id.optionsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = "${position + 1}. ${question.text}"

        val isMulti = question.type.contains("multi", ignoreCase = true)
        holder.questionType.text = if (isMulti) "Select all that apply" else "Select one"
        holder.questionType.visibility = View.VISIBLE

        holder.optionsContainer.removeAllViews()
        val context = holder.itemView.context

        if (isMulti) {
            question.options.sortedBy { it.orderIndex }.forEach { option ->
                val checkBox = CheckBox(context).apply {
                    text = option.text
                    setOnCheckedChangeListener { _, checked ->
                        val list = selectedAnswers.getOrPut(question.id) { mutableListOf() }
                        if (checked) list.add(option.id) else list.remove(option.id)
                    }
                }
                holder.optionsContainer.addView(checkBox)
            }
        } else {
            val radioGroup = RadioGroup(context).apply {
                orientation = RadioGroup.VERTICAL
            }
            question.options.sortedBy { it.orderIndex }.forEach { option ->
                val radio = RadioButton(context).apply {
                    text = option.text
                    id = View.generateViewId()
                    tag = option.id
                }
                radioGroup.addView(radio)
            }
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val optionId = group.findViewById<RadioButton>(checkedId)?.tag as? String
                if (optionId != null) {
                    selectedAnswers[question.id] = mutableListOf(optionId)
                }
            }
            holder.optionsContainer.addView(radioGroup)
        }
    }

    override fun getItemCount(): Int = questions.size
}
