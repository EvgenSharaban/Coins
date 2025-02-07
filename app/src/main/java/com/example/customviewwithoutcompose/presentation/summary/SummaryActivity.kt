package com.example.customviewwithoutcompose.presentation.summary

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivitySummaryBinding
import com.example.customviewwithoutcompose.presentation.coins.MainActivity
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
                viewModel.totalItemsCounts.collectLatest {
                    binding.tvTotalItemsCount.text = getString(R.string.total_items_count, it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hiddenCoinsCounts.collectLatest {
                    binding.tvHiddenCoinsCount.text = getString(R.string.hidden_coins_count, it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalNotesCounts.collectLatest {
                    binding.tvTotalNotesCount.text = getString(R.string.total_notes_count, it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dayWithMostNotes.collectLatest {
                    binding.tvDayWithMostNotes.text = getString(R.string.the_day_on_which_the_most_notes_were_taken, it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.amountOfDaysAppUsing.collectLatest {
                    binding.tvMemberFor.text = getString(R.string.member_for_days, it)
                }
            }
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
}