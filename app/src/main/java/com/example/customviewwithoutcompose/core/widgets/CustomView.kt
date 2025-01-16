package com.example.customviewwithoutcompose.core.widgets

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.databinding.CustomViewBinding
import kotlin.properties.Delegates

typealias ClickListener = () -> Unit

class CustomView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, R.style.DefaultCustomViewStyle)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.customViewStyle)
    constructor(context: Context) : this(context, null)

    private var binding: CustomViewBinding by Delegates.notNull()
    private var listener: ClickListener? = null

    @Suppress("MemberVisibilityCanBePrivate")
    var rankText: String = ""

    @Suppress("MemberVisibilityCanBePrivate")
    var rankTextAppearance: Int = 0

    @ColorInt
    var rankBackgroundColor: Int = 0

    @Suppress("MemberVisibilityCanBePrivate")
    var nameText: String = ""

    @Suppress("MemberVisibilityCanBePrivate")
    var descriptionText: String = ""
        set(value) {
            field = value
            binding.tvDescription.text = value
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var creationDate: String = ""

    @DrawableRes
    var logo: Int = 0
        set(value) {
            field = value
            binding.logo.setImageResource(value)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var shortNameText: String = ""

    @Suppress("MemberVisibilityCanBePrivate")
    var shortNameTextAppearance: Int = 0

    @ColorInt
    var shortNameBackgroundColor: Int = 0

    // ability to set model from code
//    var modelForCustomView: ModelForCustomView = ModelForCustomView.DEFAULT
//        set(value) {
//            field = value
//            updateView(value)
//        }

    init {
        val view = inflate(context, R.layout.custom_view, this)
        binding = CustomViewBinding.bind(view)

        initView(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.descriptionText = descriptionText
        savedState.logo = logo
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        descriptionText = savedState.descriptionText ?: "--"
        logo = savedState.logo ?: 0
    }

    // to using from activity class
//    fun setListener(listener: ClickListener) {
//        this.listener = listener
//    }

    private fun initView(attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        if (attrs == null) return

        val obtainedAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, defStyleRes)
        applyAttrs(obtainedAttrs)
        obtainedAttrs.recycle()
    }

    private fun applyAttrs(attributes: TypedArray) {
        rankText = attributes.getString(R.styleable.CustomView_rankText).orEmpty()
        rankTextAppearance = attributes.getResourceId(R.styleable.CustomView_rankTextAppearance, 0)
        rankBackgroundColor = attributes.getColor(R.styleable.CustomView_rankBackgroundColor, 0)

        nameText = attributes.getString(R.styleable.CustomView_nameText).orEmpty()
        descriptionText = attributes.getString(R.styleable.CustomView_descriptionText).orEmpty()
        creationDate = attributes.getString(R.styleable.CustomView_creationDate).orEmpty()
        logo = attributes.getResourceId(R.styleable.CustomView_logo, 0)

        shortNameText = attributes.getString(R.styleable.CustomView_shortNameText).orEmpty()
        shortNameTextAppearance = attributes.getResourceId(R.styleable.CustomView_shortNameTextAppearance, 0)
        shortNameBackgroundColor = attributes.getColor(R.styleable.CustomView_shortNameBackgroundColor, 0)

        updateView()
    }

    private fun updateView() {
        binding.rank.apply {
            text = rankText
            setTextAppearance(rankTextAppearance)
            backColor = rankBackgroundColor
        }

        binding.tvName.text = nameText
        binding.tvDescription.text = descriptionText
        binding.logo.setImageResource(logo)
        binding.tvCreationDate.text = creationDate

        binding.shortName.apply {
            text = shortNameText
            setTextAppearance(shortNameTextAppearance)
            backColor = shortNameBackgroundColor
        }
    }

    // need with modelForCustomView, if delete do it with modelForCustomView
//    private fun updateView(model: ModelForCustomView) {
//        val rank = model.rank.toString()
//        binding.rank.apply {
//            text = rank
//            setTextAppearance(model.rankTextAppearance)
//        }
//
//        binding.tvName.text = model.nameText
//        binding.tvDescription.text = model.descriptionText
//        binding.logo.load(model.logo)
//        binding.tvCreationDate.text = model.creationDate
//
//        binding.shortName.apply {
//            text = model.shortNameText
//            setTextAppearance(model.shortNameTextAppearance)
//        }
//    }

    private fun initListeners() {
        binding.container.setOnClickListener {
            this.listener?.invoke()
        }
    }

    class SavedState : BaseSavedState {

        var descriptionText: String? = null

        @DrawableRes
        var logo: Int? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            descriptionText = parcel.readString()
            logo = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(descriptionText)
            out.writeInt(logo ?: 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }
}