package com.example.customviewwithoutcompose.presentation.coins

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.fromDpToPx
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivityMainBinding
import com.example.customviewwithoutcompose.databinding.DialogAddNoteBinding
import com.example.customviewwithoutcompose.databinding.DialogDeleteNoteBinding
import com.example.customviewwithoutcompose.presentation.Events
import com.example.customviewwithoutcompose.presentation.coins.adapters.coin.CoinDelegateAdapter
import com.example.customviewwithoutcompose.presentation.coins.adapters.note.NoteDelegateAdapter
import com.example.customviewwithoutcompose.presentation.coins.models.coin.ModelForCoinsAdapter
import com.example.customviewwithoutcompose.presentation.coins.models.note.ModelForNoteCustomView
import com.example.customviewwithoutcompose.presentation.coins.models.note.mappers.NoteUiModelMapper.mapToRoomModel
import com.example.customviewwithoutcompose.presentation.summary.SummaryActivity
import com.example.delegateadapter.CompositeAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var inset: Insets
    private var isFabHidden = true

    private val coinsDelegateAdapter = CoinDelegateAdapter(::onItemCoinClicked, ::onItemCoinLongClicked)
    private val notesDelegateAdapter = NoteDelegateAdapter(::onNoteLongClicked)

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(coinsDelegateAdapter)
            .add(notesDelegateAdapter)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initBinding()
        setupEdgeToEdgeInsets()
        setupUIInsets()
        initCoinsRecyclerView()
        setupListeners()
        setupObservers()
        setupBottomNavigation()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun setupEdgeToEdgeInsets() {
        binding.root.updatePadding(true, false)
    }

    private fun setupUIInsets() {
        binding.rvCoins.updatePadding(
            needUpdateTop = false,
            needUpdateBottom = true,
            additionalBottomInset = resources.getDimensionPixelSize(R.dimen.bottom_margin_last)
        )
        // don't replace by binding.bottomNavigationView.updatePadding because there set value to global variable inset
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
            inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, inset.bottom) // changed default bottom padding for bottomNavigationView
            insets
        }
        setFabMargins(false)
        binding.rvCoins.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) { // scroll down
                    setFabMargins(true)
                } else if (dy < 0) { // scroll up
                    setFabMargins(false)
                }
            }
        })
    }

    private fun setFabMargins(isScrollDown: Boolean) {
        if (isFabHidden == isScrollDown) return
        isFabHidden = isScrollDown
        binding.fabAddNote.apply {
            val insetFab = 24.fromDpToPx(this@MainActivity).toFloat()
            val additionalBottomInset = if (isScrollDown) inset.bottom else 0
            val targetTransitionY = -insetFab - additionalBottomInset
            val targetTransitionX = -insetFab
            animate().cancel()
            animate()
                .translationY(targetTransitionY)
                .translationX(targetTransitionX)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    private fun initCoinsRecyclerView() {
        binding.rvCoins.adapter = compositeAdapter
    }

    private fun setupListeners() {
        binding.fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collectLatest { event ->
                    when (event) {
                        is Events.MessageForUser -> showMessageForUser(event.message)
                        is Events.PositionToScrolling -> binding.rvCoins.scrollToPosition(event.position)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recyclerItemsList.collectLatest { list ->
                    compositeAdapter.submitList(list)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.selectedItemId = R.id.nav_home

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_summary -> {
                    val intent = Intent(this, SummaryActivity::class.java)
                    val resetDefaultAnimation = ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle()
                    startActivity(intent, resetDefaultAnimation)
                    true
                }

                else -> false
            }
        }
    }

    private fun showMessageForUser(message: String) {
//        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        val snackBar = Snackbar.make(this@MainActivity, binding.root, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setTextMaxLines(5)
        snackBar.setAction("Ok") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    private fun onItemCoinClicked(item: ModelForCoinsAdapter) {
        viewModel.onCoinToggleExpanding(item)
    }

    private fun onItemCoinLongClicked(item: ModelForCoinsAdapter) {
        viewModel.onCoinToggleHiding(item)
    }

    private fun onNoteLongClicked(note: ModelForNoteCustomView) {
        showDeleteNoteDialog(note)
    }

    private fun showAddNoteDialog() {
        val dialogView = View.inflate(this, R.layout.dialog_add_note, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val binding = DialogAddNoteBinding.bind(dialogView)

        binding.btnConfirm.setOnClickListener {
            val noteText = binding.etItemInput.text?.toString()?.trim() ?: ""
            if (noteText.isNotEmpty()) {
                viewModel.addNote(noteText)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setDialogWindow()
    }

    private fun showDeleteNoteDialog(note: ModelForNoteCustomView) {
        val dialogView = View.inflate(this, R.layout.dialog_delete_note, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val binding = DialogDeleteNoteBinding.bind(dialogView)

        binding.btnConfirm.setOnClickListener {
            viewModel.deleteNote(note.mapToRoomModel())
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setDialogWindow()
    }

    private fun Window?.setDialogWindow() {
        this?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (resources.displayMetrics.widthPixels * WIDTH_WITH_PERCENT).toInt()
        this?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {

        private const val WIDTH_WITH_PERCENT = 0.9
    }

}