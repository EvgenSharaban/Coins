package com.example.customviewwithoutcompose

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customviewwithoutcompose.databinding.ActivityMainBinding
import com.example.customviewwithoutcompose.models.CustomViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        binding.customView.customViewModel = CustomViewModel(
            rankText = "3",
            rankTextSize = 30,
            rankBackgroundColor = getColor(R.color.yellow),
            nameText = "Bitcoin",
            creationDate = "Since 2009",
            logo = R.drawable.ic_avatar_test_user,
            shortNameText = "VVV",
            shortNameTextSize = 18,
            shortNameBackgroundColor = getColor(R.color.white)
        )
    }
}