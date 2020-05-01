package com.czyzewski.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.czyzewski.ui.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
    }

    fun setErrorText(name: String) {
        errorText.text = name
    }
}
