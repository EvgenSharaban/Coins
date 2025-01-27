package com.example.customviewwithoutcompose.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.formatDate
import com.example.customviewwithoutcompose.databinding.CustomViewBinding
import com.example.customviewwithoutcompose.databinding.ItemNoteBinding
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForAdapter
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

class CoinsListAdapter(
    private val onCoinClicked: (item: ModelForAdapter) -> Unit,
    private val onNoteLongClicked: (item: ModelForNoteCustomView) -> Unit
) :
    ListAdapter<CustomListItem, RecyclerView.ViewHolder>(CoinDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CustomListItem.NoteItem -> TYPE_NOTE
            is CustomListItem.CoinItem -> TYPE_COIN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NOTE -> {
                val view = inflater.inflate(R.layout.item_note, parent, false)
                NoteViewHolder(ItemNoteBinding.bind(view), onNoteLongClicked)
            }

            TYPE_COIN -> {
                val view = inflater.inflate(R.layout.custom_view, parent, false)
                CoinViewHolder(CustomViewBinding.bind(view), onCoinClicked)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                val noteItem = getItem(position) as CustomListItem.NoteItem
                holder.bind(noteItem.note.note)
            }

            is CoinViewHolder -> {
                val coinItem = getItem(position) as CustomListItem.CoinItem
                holder.bind(coinItem.coin)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any?>) {
        if (holder is CoinViewHolder && payloads.isNotEmpty() && payloads[0] == DESCRIPTION_VISIBILITY_PAYLOAD) {
            holder.toggleVisibility((getItem(position) as CustomListItem.CoinItem).coin.isExpanded)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class NoteViewHolder(
        private val binding: ItemNoteBinding,
        onClick: (item: ModelForNoteCustomView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnLongClickListener {
                val item = (getItem(adapterPosition) as CustomListItem.NoteItem).note
                onClick(item)
                true
            }
        }

        fun bind(note: String) {
            binding.tvNote.text = note
        }
    }


    inner class CoinViewHolder(
        private val binding: CustomViewBinding,
        onClick: (item: ModelForAdapter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val item = (getItem(adapterPosition) as CustomListItem.CoinItem).coin
                onClick(item)
            }
        }

        fun bind(model: ModelForAdapter) {
            val rankText = model.customViewModel.rank.toString()
            binding.rank.apply {
                text = rankText
                setTextAppearance(model.customViewModel.rankTextAppearance)
            }

            binding.tvName.text = model.customViewModel.nameText
            binding.tvDescription.isVisible = model.isExpanded
            binding.tvDescription.text = model.customViewModel.descriptionText
            binding.price.text = itemView.context.getString(R.string.price_for_coin, model.customViewModel.price.toString())
            binding.logo.load(model.customViewModel.logo)
            binding.tvCreationDate.text = itemView.context.getString(R.string.coin_created_at, formatDate(model.customViewModel.creationDate))

            binding.shortName.apply {
                text = model.customViewModel.shortNameText
                setTextAppearance(model.customViewModel.shortNameTextAppearance)
            }
        }

        fun toggleVisibility(isExpanded: Boolean) {
            binding.tvDescription.isVisible = isExpanded
        }
    }

    private class CoinDiffUtil : DiffUtil.ItemCallback<CustomListItem>() {
        override fun areItemsTheSame(oldItem: CustomListItem, newItem: CustomListItem): Boolean {
            return when {
                oldItem is CustomListItem.NoteItem && newItem is CustomListItem.NoteItem -> oldItem.note == newItem.note
                oldItem is CustomListItem.CoinItem && newItem is CustomListItem.CoinItem ->
                    oldItem.coin.customViewModel.id == newItem.coin.customViewModel.id

                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: CustomListItem,
            newItem: CustomListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CustomListItem, newItem: CustomListItem): Any? {
            if (
                oldItem is CustomListItem.CoinItem &&
                newItem is CustomListItem.CoinItem &&
                oldItem.coin.isExpanded != newItem.coin.isExpanded
            ) {
                return DESCRIPTION_VISIBILITY_PAYLOAD
            }
            return null
        }
    }

    companion object {

        private const val DESCRIPTION_VISIBILITY_PAYLOAD = "Description visibility changed"
        private const val TYPE_NOTE = 1
        private const val TYPE_COIN = 2
    }
}