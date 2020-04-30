package com.czyzewski.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getStringOrThrow
import kotlinx.android.synthetic.main.view_toolbar.view.*

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_toolbar, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarView)
        with(typedArray) {
            title.text = getStringOrThrow(R.styleable.ToolbarView_tv_title)
        }
        typedArray.recycle()
    }

    fun setRateLimit(timeToReset: String, remaining: Int, total: Int) {
        title.text = "$remaining/$total requests left\nreset : $timeToReset"
    }

    fun onBackClick(action: () -> Unit) = backIcon.setOnClickListener { action() }
}
