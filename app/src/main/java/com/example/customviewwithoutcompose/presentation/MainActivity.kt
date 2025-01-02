package com.example.customviewwithoutcompose.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.ActivityMainBinding
import com.example.customviewwithoutcompose.presentation.uimodels.ModelForCustomView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.customView.modelForCustomView = ModelForCustomView(
//            rankText = "3",
//            rankTextAppearance = R.style.RankTextAppearance,
//            rankBackgroundColor = getColor(R.color.yellow),
//            nameText = "Bitcoin",
//            descriptionText = "Description",
//            creationDate = "Since 2009",
//            logo = R.drawable.ic_avatar_test_user,
//            shortNameText = "BTC",
//            shortNameTextAppearance = R.style.ShortNameTextAppearance,
//            shortNameBackgroundColor = getColor(R.color.white)
//        )

        viewModel.coinModel.observe(this) { coin ->
            if (coin != null) {
                binding.customView.modelForCustomView = ModelForCustomView(
                    rankText = coin.rank.toString(),
                    rankTextAppearance = R.style.RankTextAppearance,
                    rankBackgroundColor = getColor(R.color.yellow),
                    nameText = coin.name ?: "",
                    descriptionText = coin.description ?: "",
                    creationDate = "Since 2009",
                    logo = R.drawable.ic_avatar_test_user,
                    shortNameText = coin.symbol ?: "",
                    shortNameTextAppearance = R.style.ShortNameTextAppearance,
                    shortNameBackgroundColor = getColor(R.color.white)
                )
                binding.customView.descriptionText = coin.description ?: ""
            }
        }

//        setListeners()
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewModel.event.collectLatest { message ->
                showToast(message)
            }
        }
    }

    private fun setListeners() {
        binding.customView.setListener {
            binding.customView.descriptionText = "Description view clicked"
            binding.customView.logo = R.drawable.case_detail_sample
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}