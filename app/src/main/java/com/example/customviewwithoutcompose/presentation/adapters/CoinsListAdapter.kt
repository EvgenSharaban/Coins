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
import com.example.customviewwithoutcompose.presentation.models.ModelForAdapter

class CoinsListAdapter(private val onClick: (item: ModelForAdapter) -> Unit) :
    ListAdapter<ModelForAdapter, CoinsListAdapter.CoinViewHolder>(CoinDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view, parent, false)
        return CoinViewHolder(CustomViewBinding.bind(view), onClick)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }


    inner class CoinViewHolder(
        private val binding: CustomViewBinding,
        onClick: (item: ModelForAdapter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(getItem(adapterPosition))
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
    }

    private class CoinDiffUtil : DiffUtil.ItemCallback<ModelForAdapter>() {
        override fun areItemsTheSame(
            oldItem: ModelForAdapter,
            newItem: ModelForAdapter
        ): Boolean {
            return oldItem.customViewModel.id == newItem.customViewModel.id
        }

        override fun areContentsTheSame(
            oldItem: ModelForAdapter,
            newItem: ModelForAdapter
        ): Boolean {
            return oldItem == newItem
        }
    }
}