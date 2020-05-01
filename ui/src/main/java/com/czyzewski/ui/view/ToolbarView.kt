package com.czyzewski.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getStringOrThrow
import androidx.core.view.isVisible
import com.czyzewski.ui.R
import com.czyzewski.ui.models.RateLimitUiModel
import kotlinx.android.synthetic.main.view_toolbar.view.*

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var inSearchMode : Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_toolbar, this)
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ToolbarView
        )
        with(typedArray) {
            title.text = getStringOrThrow(R.styleable.ToolbarView_tv_title)
            searchIcon.isVisible = getBoolean(R.styleable.ToolbarView_tv_searchAvailable, false)
        }
        typedArray.recycle()
    }

    fun setRateLimit(uiModel: RateLimitUiModel) {
        title.isVisible = true
        title.text = with(uiModel) {
            "$remaining/$total requests left\nreset : $timeToReset"
        }
        toolbarErrorView.isVisible = false
    }

    fun showProgress() {
        title.isVisible = false
        toolbarProgressView.isVisible = true
        toolbarErrorView.isVisible = false
    }

    fun hideProgress() {
        toolbarProgressView.isVisible = false
        toolbarErrorView.isVisible = false
    }

    fun showError() {
        title.isVisible = false
        toolbarProgressView.isVisible = false
        toolbarErrorView.isVisible = true
    }

    fun enterSearchMode() {
        inSearchMode = true
        searchInput.isVisible = true
        refreshIcon.isVisible = false
        title.isVisible = false
        toolbarProgressView.isVisible = false
        toolbarErrorView.isVisible = false
    }

    fun exitSearchMode() {
        inSearchMode = false
        searchInput.isVisible = false
        refreshIcon.isVisible = true
        title.isVisible = true
        toolbarProgressView.isVisible = false
        toolbarErrorView.isVisible = false
    }

    fun onBackClick(action: () -> Unit) = backIcon.setOnClickListener { action() }

    fun onRefreshClick(action: () -> Unit) = refreshIcon.setOnClickListener { action() }

    fun onSearchClick(action: (Boolean) -> Unit) = searchIcon.setOnClickListener { action(inSearchMode) }

    fun onSearchInputChanged(action: (String) -> Unit) = searchInput.afterTextChanged { action(it) }


    private fun EditText.afterTextChanged(action: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                action(p0.toString())
            }
        })
    }
}
