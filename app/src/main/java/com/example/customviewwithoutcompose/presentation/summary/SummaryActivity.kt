package com.example.customviewwithoutcompose.presentation.summary

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.core.other.showSnackBar
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivitySummaryBinding
import com.example.customviewwithoutcompose.presentation.coins.MainActivity
import com.example.customviewwithoutcompose.presentation.summary.utility.EventsSummary
import com.example.customviewwithoutcompose.presentation.summary.utility.SummaryState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private val viewModel: SummaryActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initBinding()
        setObservers()
        setupEdgeToEdgeInsets()
        setupUiInsets()
        setupBottomNavigation()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveToHome()
            }
        })
    }

    private fun initBinding() {
        binding = ActivitySummaryBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collectLatest { event ->
                    when (event) {
                        is EventsSummary.MessageForUser -> showSnackBar(binding.root, event.message)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.summaryUiState.collectLatest { state ->
                    setViews(state)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    private fun setupEdgeToEdgeInsets() {
        binding.root.updatePadding(true, false)
    }

    private fun setupUiInsets() {
        binding.bottomNavigationView.updatePadding(
            needUpdateTop = false,
            needUpdateBottom = true
        )
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.selectedItemId = R.id.nav_summary

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    moveToHome()
                    true
                }

                else -> false
            }
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, MainActivity::class.java)
        val resetDefaultAnimation = ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle()
        startActivity(intent, resetDefaultAnimation)
    }

    private fun setViews(state: SummaryState) {
        when (state) {
            is SummaryState.Default -> {
                binding.tvTotalItemsCount.isVisible = false
                binding.tvHiddenCoinsCount.isVisible = false
                binding.tvTotalNotesCount.isVisible = false
                binding.tvDayWithMostNotes.isVisible = false
                binding.tvMemberFor.isVisible = false
            }

            is SummaryState.Loaded -> {
                setView(binding.tvTotalItemsCount, state.value.totalItemsCounts, R.string.total_items_count)
                setView(binding.tvHiddenCoinsCount, state.value.hiddenCoinsCounts, R.string.hidden_coins_count)
                setView(binding.tvTotalNotesCount, state.value.totalNotesCounts, R.string.total_notes_count)
                setView(binding.tvDayWithMostNotes, state.value.dayWithMostNotes, R.string.the_day_on_which_the_most_notes_were_taken)
                setView(binding.tvMemberFor, state.value.amountOfDaysAppUsing, R.string.member_for_days)
            }
        }
    }

    private fun setView(view: AppCompatTextView, value: String, @StringRes resource: Int) {
        val text = if (value == FAILURE_VALUE) {
            getString(resource, value).substringBefore(value) + value
        } else {
            getString(resource, value)
        }
        view.text = text
        view.isVisible = true
    }
}