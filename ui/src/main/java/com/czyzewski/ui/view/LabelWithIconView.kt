package com.czyzewski.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.czyzewski.ui.R
import com.czyzewski.ui.view.LabelWithIconView.ViewAttributes
import kotlinx.android.synthetic.main.view_icon_with_label.view.*

interface CustomView<ViewAttributes> {
    fun getViewAttributes(): ViewAttributes
    fun extractAttributes(viewAttributes: ViewAttributes)
}

class LabelWithIconView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CustomView<ViewAttributes> {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_icon_with_label, this)
        extractAttributes(getViewAttributes())
    }

    override fun extractAttributes(viewAttributes: ViewAttributes) {
        viewAttributes.apply {
            labelText?.let { labelView.text = it }
            iconRes?.let { iconView.setImageResource(it) }
        }
    }

    override fun getViewAttributes(): ViewAttributes {
        context.obtainStyledAttributes(attrs, R.styleable.LabelWithIconView).apply {
            val attributes = ViewAttributes(
                labelText = getString(R.styleable.LabelWithIconView_lwi_label),
                iconRes = getResourceIdOrNull(R.styleable.LabelWithIconView_lwi_icon)
            )
            recycle()
            return attributes
        }
    }

    fun setOrHide(@DrawableRes iconResId: Int, text: String?) {
        text?.let {
            labelView.text = text
            iconView.setImageDrawable(ContextCompat.getDrawable(context, iconResId))
        } ?: hide()
    }

    private fun hide() {
        isVisible = false
    }


    data class ViewAttributes(
        val labelText: String?,
        @DrawableRes val iconRes: Int?
    )
}


fun TypedArray.getResourceIdOrNull(index: Int): Int? =
    getResourceId(index, Int.MIN_VALUE).takeIf { it != Int.MIN_VALUE }
