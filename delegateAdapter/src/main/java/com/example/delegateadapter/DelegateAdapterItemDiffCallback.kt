package com.example.delegateadapter

import androidx.recyclerview.widget.DiffUtil

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<DelegateAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem
    ): Boolean {
        return oldItem::class == newItem::class &&
                oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem
    ): Boolean {
        return oldItem.content() == newItem.content()
    }

    override fun getChangePayload(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Any? {
        return oldItem.payload(newItem)
    }
}