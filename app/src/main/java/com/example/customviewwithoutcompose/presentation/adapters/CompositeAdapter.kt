package com.example.customviewwithoutcompose.presentation.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>
) : ListAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can't find view type for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any?>) {
        val delegatesAdapter = delegates[getItemViewType(position)]

        if (delegatesAdapter != null) {
            val delegatePayloads = payloads.map { it as DelegateAdapterItem.PayloadAble }
            delegatesAdapter.bindViewHolder(getItem(position), holder, delegatePayloads)
        } else {
            throw NullPointerException("can't find adapter for position $position")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewAttachedToWindow(holder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>> = SparseArray()

//        @Suppress("UNCHECKED_CAST")
        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, *>): Builder {
            // what else can be done besides an annotation above?
            delegates.put(count++, delegateAdapter as DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>)
            return this
        }

        fun build(): CompositeAdapter {
            // this is alternative if (count == 0) throw IllegalArgumentException("Register at least one adapter")
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }

}