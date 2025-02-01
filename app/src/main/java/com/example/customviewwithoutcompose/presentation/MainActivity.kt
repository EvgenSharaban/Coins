package com.example.customviewwithoutcompose.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivityMainBinding
import com.example.customviewwithoutcompose.databinding.DialogAddNoteBinding
import com.example.customviewwithoutcompose.databinding.DialogDeleteNoteBinding
import com.example.customviewwithoutcompose.presentation.adapters.CompositeAdapter
import com.example.customviewwithoutcompose.presentation.adapters.coin.CoinDelegateAdapter
import com.example.customviewwithoutcompose.presentation.adapters.note.NoteDelegateAdapter
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView
import com.example.customviewwithoutcompose.presentation.models.note.mappers.NoteUiModelMapper.mapToRoomModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private val coinsDelegateAdapter = CoinDelegateAdapter(::onItemCoinClicked)
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
                        is Events.MessageForUser -> showToast(event.message)
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
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun onItemCoinClicked(item: ModelForCoinsAdapter) {
        viewModel.onItemToggle(item)
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