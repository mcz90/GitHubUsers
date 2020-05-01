package com.czyzewski.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.czyzewski.ui.R
import kotlinx.android.synthetic.main.view_rounded_header_with_label.view.*

class RoundedHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var valueIncreaseAnimation: ValueAnimator

    init {
        LayoutInflater.from(context).inflate(R.layout.view_rounded_header_with_label, this)
    }

    fun setHeaderText(value: Long) {
        val maxValue = value.takeIf { it <= 99 }?.toInt() ?: 99
        valueIncreaseAnimation = ValueAnimator.ofInt(0, maxValue).apply {
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                header.text = if (value <= 99) {
                    animation?.animatedValue.toString()
                } else {
                    animation?.animatedValue.toString().plus("+")
                }
            }

            duration = 2000
            start()
        }
    }

    fun setLabelText(text: String) {
        label.text = text
    }
}
