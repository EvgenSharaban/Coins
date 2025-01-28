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
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter

class CoinsAdapter(
    private val onCoinClicked: (item: ModelForCoinsAdapter) -> Unit
) : ListAdapter<CustomListItem.CoinItem, CoinsAdapter.CoinViewHolder>(CoinDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_view, parent, false)
        return CoinViewHolder(CustomViewBinding.bind(view), onCoinClicked)
    }

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int
    ) {
        val coinItem = getItem(position)
        holder.bind(coinItem.coin)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int, payloads: List<Any?>) {
        if (payloads.isNotEmpty() && payloads[0] == DESCRIPTION_VISIBILITY_PAYLOAD) {
            holder.toggleVisibility(getItem(position).coin.isExpanded)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class CoinViewHolder(
        private val binding: CustomViewBinding,
        private val onClicked: (item: ModelForCoinsAdapter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position).coin
                    onClicked(item)
                }
            }
        }

        fun bind(model: ModelForCoinsAdapter) {
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

    private class CoinDiffUtil : DiffUtil.ItemCallback<CustomListItem.CoinItem>() {
        override fun areItemsTheSame(
            oldItem: CustomListItem.CoinItem,
            newItem: CustomListItem.CoinItem
        ): Boolean {
            return oldItem.coin.customViewModel.id == newItem.coin.customViewModel.id
        }

        override fun areContentsTheSame(
            oldItem: CustomListItem.CoinItem,
            newItem: CustomListItem.CoinItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: CustomListItem.CoinItem,
            newItem: CustomListItem.CoinItem
        ): Any? {
            return if (oldItem.coin.isExpanded != newItem.coin.isExpanded) {
                DESCRIPTION_VISIBILITY_PAYLOAD
            } else {
                null
            }
        }
    }

    companion object {

        private const val DESCRIPTION_VISIBILITY_PAYLOAD = "Description visibility changed"
    }
}