package com.example.customviewwithoutcompose.presentation.adapters.coin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.formatDate
import com.example.customviewwithoutcompose.databinding.CustomViewBinding
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter
import com.example.delegateadapter.DelegateAdapter
import com.example.delegateadapter.DelegateAdapterItem

class CoinDelegateAdapter(
    private val onCoinClicked: (item: ModelForCoinsAdapter) -> Unit
) : DelegateAdapter<CoinItem, CoinDelegateAdapter.CoinViewHolder>(CoinItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_view, parent, false)
        return CoinViewHolder(CustomViewBinding.bind(view), onCoinClicked)
    }

    override fun bindViewHolder(
        model: CoinItem,
        viewHolder: CoinViewHolder,
        payloads: List<DelegateAdapterItem.PayloadAble>
    ) {
        when (val payload = payloads.firstOrNull() as? CoinItem.ChangePayload) {
            is CoinItem.ChangePayload.CoinExpandChanged -> viewHolder.toggleVisibility(payload.isExpanded)
            else -> viewHolder.bind(model.coin)
        }
    }


    inner class CoinViewHolder(
        private val binding: CustomViewBinding,
        private val onClicked: (item: ModelForCoinsAdapter) -> Unit,

        ) : RecyclerView.ViewHolder(binding.root) {

        private var localCoinItem: ModelForCoinsAdapter? = null

        init {
            itemView.setOnClickListener {
                // if this not need - delete after checking
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
                val item = localCoinItem ?: return@setOnClickListener
                onClicked(item)
//                }
            }
        }

        fun bind(model: ModelForCoinsAdapter) {
            localCoinItem = model
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
//            localCoinItem = localCoinItem?.copy(isExpanded = isExpanded) // if this not need - delete after checking
        }
    }
}