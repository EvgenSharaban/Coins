package com.example.customviewwithoutcompose.presentation

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoratorCoinsList(
    private val bottomValueInDPLastItem: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        /** cast margin to dp */
        val bottomSpase = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            bottomValueInDPLastItem.toFloat(),
            view.resources.displayMetrics
        ).toInt()

        /** adding marginTop to first item and marginBottom to last item */
        when (itemPosition) {
            state.itemCount - 1 -> outRect.bottom = bottomSpase
        }
    }
}