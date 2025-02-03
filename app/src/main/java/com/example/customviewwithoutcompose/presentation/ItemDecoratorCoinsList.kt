package com.example.customviewwithoutcompose.presentation

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemDecoratorCoinsList(
    private val bottomMarginPx: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapter = parent.adapter ?: return
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        /** adding marginBottom to last item */
        when (itemPosition) {
            adapter.itemCount - 1 -> outRect.bottom = bottomMarginPx
            else -> outRect.bottom = 0
        }
    }
}