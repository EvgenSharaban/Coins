package com.example.customviewwithoutcompose.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.CustomViewBinding
import com.example.customviewwithoutcompose.models.CustomViewModel
import kotlin.properties.Delegates

class CustomView (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, R.style.DefaultCustomViewStyle)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, R.attr.customViewStyle)
    constructor(context: Context): this(context, null)

    private var binding: CustomViewBinding by Delegates.notNull()

    @Suppress("MemberVisibilityCanBePrivate")
    var rankText: String = ""
    @Suppress("MemberVisibilityCanBePrivate")
    var rankTextSize: Int = 0
    @ColorInt
    var rankBackgroundColor: Int = 0

    @Suppress("MemberVisibilityCanBePrivate")
    var nameText: String = ""
    @Suppress("MemberVisibilityCanBePrivate")
    var creationDate: String = ""
    @DrawableRes
    var logo: Int = 0

    @Suppress("MemberVisibilityCanBePrivate")
    var shortNameText: String = ""
    @Suppress("MemberVisibilityCanBePrivate")
    var shortNameTextSize: Int = 0
    @ColorInt
    var shortNameBackgroundColor: Int = 0

    var customViewModel: CustomViewModel = CustomViewModel.DEFAULT
        set(value) {
            field = value
            updateView(value)
        }

    init {
        val view = inflate(context, R.layout.custom_view, this)
        binding = CustomViewBinding.bind(view)

        initView(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        if (attrs == null) return

        val obtainedAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, defStyleRes)
        applyAttrs(obtainedAttrs)
        obtainedAttrs.recycle()
    }

    private fun applyAttrs(attributes: TypedArray) {
        rankText = attributes.getString(R.styleable.CustomView_rankText).orEmpty()
        rankTextSize = attributes.getInteger(R.styleable.CustomView_rankTextSize, 8)
        rankBackgroundColor = attributes.getColor(R.styleable.CustomView_rankBackgroundColor, 0)

        nameText = attributes.getString(R.styleable.CustomView_nameText).orEmpty()
        creationDate = attributes.getString(R.styleable.CustomView_creationDate).orEmpty()
        logo = attributes.getResourceId(R.styleable.CustomView_logo, 0)

        shortNameText = attributes.getString(R.styleable.CustomView_shortNameText).orEmpty()
        shortNameTextSize = attributes.getInteger(R.styleable.CustomView_shortNameTextSize, 8)
        shortNameBackgroundColor = attributes.getColor(R.styleable.CustomView_shortNameBackgroundColor, 0)

        updateView()
    }

    private fun updateView() {
        binding.crvRank.apply {
            textRhombus = rankText
            textRhombusSize = rankTextSize
            backgroundRhombusColor = rankBackgroundColor
        }

        binding.tvName.text = nameText
        binding.logo.setImageResource(logo)
        binding.tvCreationDate.text = creationDate

        binding.crvShortName.apply {
            textRhombus = shortNameText
            textRhombusSize = shortNameTextSize
            backgroundRhombusColor = shortNameBackgroundColor
        }
    }

    private fun updateView(model: CustomViewModel) {
        binding.crvRank.apply {
            textRhombus = model.rankText
            textRhombusSize = model.rankTextSize
            backgroundRhombusColor = model.rankBackgroundColor
        }

        binding.tvName.text = model.nameText
        binding.logo.setImageResource(model.logo)
        binding.tvCreationDate.text = model.creationDate

        binding.crvShortName.apply {
            textRhombus = model.shortNameText
            textRhombusSize = model.shortNameTextSize
            backgroundRhombusColor = model.shortNameBackgroundColor
        }
    }

}