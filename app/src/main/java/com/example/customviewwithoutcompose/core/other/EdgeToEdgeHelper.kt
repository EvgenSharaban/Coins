package com.example.customviewwithoutcompose.core.other

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding


fun View.updatePadding(
    needUpdateTop: Boolean,
    needUpdateBottom: Boolean,
    additionalTopInset: Int = 0,
    additionalBottomInset: Int = 0
) {
    updatePadding(top = 0, bottom = 0)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val insetsBottomWithAdditional = additionalBottomInset

        when {
            needUpdateTop && needUpdateBottom -> view.updatePadding(
                top = insets.top + additionalTopInset,
                bottom = insetsBottomWithAdditional
            )

            needUpdateTop -> view.updatePadding(top = insets.top + additionalTopInset)
            needUpdateBottom -> view.updatePadding(bottom = insetsBottomWithAdditional)
        }
        windowInsets
    }
    requestApplyInsetsWhenAttached()
}

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}