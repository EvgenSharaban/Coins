package com.example.customviewwithoutcompose.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.CustomRhombusViewBinding
import kotlin.properties.Delegates

class CustomRhombusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: CustomRhombusViewBinding by Delegates.notNull()

    var textRhombus: String = ""
        set(value) {
            field = value
            updateView()

        }

    var textRhombusSize: Int = 0
        set(value) {
            field = value
            updateView()
        }

    @ColorInt
    var backgroundRhombusColor: Int = 0
        set(value) {
            field = value
            updateView()
        }

    init {
        val view = inflate(context, R.layout.custom_rhombus_view, this)
        binding = CustomRhombusViewBinding.bind(view)

        initView(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        if (attrs == null) return

        val obtainedAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomRhombusView, defStyleAttr, defStyleRes)
        applyAttrs(obtainedAttrs)
        obtainedAttrs.recycle()
    }

    private fun applyAttrs(attributes: TypedArray) {
        textRhombus = attributes.getString(R.styleable.CustomRhombusView_textRhombus).orEmpty()
        textRhombusSize = attributes.getInt(R.styleable.CustomRhombusView_textRhombusSize, 16)

        backgroundRhombusColor = attributes.getColor(
            R.styleable.CustomRhombusView_backgroundRhombusColor,
            ContextCompat.getColor(context, R.color.teal_200)
        )
        updateView()
    }

    private fun updateView() {
        binding.tvRhombus.text = textRhombus
        binding.tvRhombus.textSize = textRhombusSize.toFloat()

        binding.rhombusView.backColor = backgroundRhombusColor
    }
}