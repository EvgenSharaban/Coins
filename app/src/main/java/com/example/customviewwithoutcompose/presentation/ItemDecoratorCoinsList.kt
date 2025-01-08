package com.example.customviewwithoutcompose.presentation

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemDecoratorCoinsList(
    @DimenRes private val bottomMarginResId: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        /** cast margin in dp to Int */
//        val bottomSpase = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            bottomValueInDPLastItem.toFloat(),
//            view.resources.displayMetrics
//        ).toInt()

        val bottomSpase = view.resources.getDimensionPixelSize(bottomMarginResId)

        /** adding marginBottom to last item */
        when (itemPosition) {
            state.itemCount - 1 -> outRect.bottom = bottomSpase
        }
    }
}