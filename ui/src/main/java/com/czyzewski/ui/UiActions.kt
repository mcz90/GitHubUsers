package com.czyzewski.ui

import android.view.View
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
private fun View.clicks() = callbackFlow {
    this@clicks.setOnClickListener {
        this.offer(Unit)
    }
    awaitClose { this@clicks.setOnClickListener(null) }
}
