package com.example.customviewwithoutcompose.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.ItemNoteBinding
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

class NotesAdapter(
    private val onNoteLongClicked: (item: ModelForNoteCustomView) -> Unit
) : ListAdapter<CustomListItem.NoteItem, NotesAdapter.NoteViewHolder>(NoteDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(ItemNoteBinding.bind(view), onNoteLongClicked)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position).note.note)
    }

    inner class NoteViewHolder(
        private val binding: ItemNoteBinding,
        onClick: (item: ModelForNoteCustomView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    onClick(item.note)
                    true
                } else {
                    false
                }
            }
        }

        fun bind(note: String) {
            binding.tvNote.text = note
        }
    }

    private class NoteDiffUtil : DiffUtil.ItemCallback<CustomListItem.NoteItem>() {
        override fun areItemsTheSame(
            oldItem: CustomListItem.NoteItem,
            newItem: CustomListItem.NoteItem
        ): Boolean {
            return oldItem.note.id == newItem.note.id
        }

        override fun areContentsTheSame(
            oldItem: CustomListItem.NoteItem,
            newItem: CustomListItem.NoteItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}