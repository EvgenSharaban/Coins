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
import com.example.customviewwithoutcompose.presentation.uimodels.ModelForCustomView

class CoinsListAdapter : ListAdapter<ModelForCustomView, CoinsListAdapter.CoinViewHolder>(CoinDiffUtil()) {

    private var expandedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view, parent, false)
        return CoinViewHolder(CustomViewBinding.bind(view))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

        val isExpanded = position == expandedPosition
        holder.toggleVisibility(isExpanded)

        holder.itemView.setOnClickListener {
            val previousPosition = expandedPosition
            expandedPosition = if (expandedPosition == position) {
                RecyclerView.NO_POSITION
            } else {
                position
            }

            notifyItemChanged(previousPosition)
            notifyItemChanged(expandedPosition)
        }
    }


    inner class CoinViewHolder(
        private val binding: CustomViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ModelForCustomView) {
            val rankText = model.rank.toString()
            binding.rank.apply {
                text = rankText
                setTextAppearance(model.rankTextAppearance)
            }

            binding.tvName.text = model.nameText
            binding.tvDescription.text = model.descriptionText
            binding.price.text = itemView.context.getString(R.string.price_for_coin, model.price.toString())
            binding.logo.load(model.logo)
            binding.tvCreationDate.text = itemView.context.getString(R.string.coin_created_at, formatDate(model.creationDate))

            binding.shortName.apply {
                text = model.shortNameText
                setTextAppearance(model.shortNameTextAppearance)
            }
        }

        fun toggleVisibility(isExpanded: Boolean) {
            binding.tvDescription.isVisible = isExpanded
        }
    }

    private class CoinDiffUtil : DiffUtil.ItemCallback<ModelForCustomView>() {
        override fun areItemsTheSame(
            oldItem: ModelForCustomView,
            newItem: ModelForCustomView
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ModelForCustomView,
            newItem: ModelForCustomView
        ): Boolean {
            return oldItem == newItem
        }
    }
}