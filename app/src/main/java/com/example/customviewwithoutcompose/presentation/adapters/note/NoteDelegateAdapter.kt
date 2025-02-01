package com.example.customviewwithoutcompose.presentation.adapters.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.ItemNoteBinding
import com.example.customviewwithoutcompose.presentation.adapters.DelegateAdapter
import com.example.customviewwithoutcompose.presentation.adapters.DelegateAdapterItem
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

class NoteDelegateAdapter(
    private val onNoteLongClicked: (item: ModelForNoteCustomView) -> Unit
) : DelegateAdapter<NoteItem, NoteDelegateAdapter.NoteViewHolder>(NoteItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(ItemNoteBinding.bind(view), onNoteLongClicked)
    }

    override fun bindViewHolder(
        model: NoteItem,
        viewHolder: NoteViewHolder,
        payloads: List<DelegateAdapterItem.PayloadAble>
    ) {
        viewHolder.bind(model.note)
    }


    inner class NoteViewHolder(
        private val binding: ItemNoteBinding,
        onClick: (item: ModelForNoteCustomView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var localItem: ModelForNoteCustomView? = null

        init {
            itemView.setOnLongClickListener {
                // if this not need - delete after checking
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
                val item = localItem ?: return@setOnLongClickListener false
                onClick(item)
                true
//                } else {
//                    false
//                }
            }
        }

        fun bind(note: ModelForNoteCustomView) {
            localItem = note
            binding.tvNote.text = note.note
        }
    }
}