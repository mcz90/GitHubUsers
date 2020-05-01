package com.czyzewski.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addScrollToBottomListener(onScrolledToBottom: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isScrolledToBottom(dy)) {
                onScrolledToBottom()
            }
        }

        private fun isScrolledToBottom(dy: Int) =
            adapter?.let {
                require(layoutManager is LinearLayoutManager)
                it.itemCount > 0 && dy > 0 &&
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == it.itemCount - 1
            } ?: false
    })
}
