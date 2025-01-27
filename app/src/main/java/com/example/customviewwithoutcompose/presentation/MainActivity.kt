package com.example.customviewwithoutcompose.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.updatePadding
import com.example.customviewwithoutcompose.databinding.ActivityMainBinding
import com.example.customviewwithoutcompose.presentation.adapters.CoinsListAdapter
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForAdapter
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView
import com.example.customviewwithoutcompose.presentation.models.note.mappers.NoteUiModelMapper.mapToRoomModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val coinsAdapter = CoinsListAdapter(
        onCoinClicked = ::onItemCoinClicked,
        onNoteLongClicked = { note ->
            onNoteLongClicked(note)
        }
    )
    private lateinit var coinsDecorator: ItemDecoratorCoinsList

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        coinsDecorator = ItemDecoratorCoinsList(R.dimen.bottom_margin_last)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvCoins.apply {
            adapter = coinsAdapter
            addItemDecoration(coinsDecorator)
        }

        binding.root.updatePadding(true, false)

        binding.fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messageForUser.collectLatest { message ->
                    showToast(message)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.positionToScrollingList.collectLatest { position ->
                    binding.rvCoins.scrollToPosition(position)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recyclerItemsList.collectLatest { list ->
                    coinsAdapter.submitList(list)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun onItemCoinClicked(item: ModelForAdapter) {
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

        val etNoteInput = dialogView.findViewById<AppCompatEditText>(R.id.etItemInput)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            val noteText = etNoteInput.text.toString().trim()
            if (noteText.isNotEmpty()) {
                viewModel.addNote(noteText)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener {
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

        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            viewModel.deleteNote(note.mapToRoomModel())
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
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