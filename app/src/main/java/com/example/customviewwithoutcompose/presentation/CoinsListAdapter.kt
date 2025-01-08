package com.example.customviewwithoutcompose.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.CustomViewBinding
import com.example.customviewwithoutcompose.presentation.CoinsListAdapter.CoinViewHolder
import com.example.customviewwithoutcompose.presentation.uimodels.ModelForCustomView

class CoinsListAdapter : ListAdapter<ModelForCustomView, CoinViewHolder>(CoinDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
//                val view = CustomView(parent.context)
//                view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view, parent, false)
        return CoinViewHolder(CustomViewBinding.bind(view))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
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
            binding.logo.load(model.logo)
            binding.tvCreationDate.text = model.creationDate

            binding.shortName.apply {
                text = model.shortNameText
                setTextAppearance(model.shortNameTextAppearance)
            }
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