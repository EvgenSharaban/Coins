package com.example.customviewwithoutcompose.presentation.summary

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.fromDpToPx
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivitySummaryBinding
import com.example.customviewwithoutcompose.presentation.coins.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initBinding()
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

    private fun setupEdgeToEdgeInsets() {
        binding.root.updatePadding(true, false)
    }

    private fun setupUiInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
            view.setPadding(0, 0, 0, 8.fromDpToPx(this)) // changed default bottom padding for bottomNavigationView
            insets
        }
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