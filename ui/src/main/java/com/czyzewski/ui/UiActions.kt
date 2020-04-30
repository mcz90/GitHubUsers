package com.czyzewski.ui

import android.view.View
import com.czyzewski.ui.Actions.Companion.click

fun View.longClick(): Actions<Unit> {
    val click = click<Unit>()
    setOnLongClickListener {
        click.backlash(Unit)
        true
    }
    return click
}

fun View.click(count: Int = 1, clicksIntervalMillis: Long = 200): Actions<Unit> {
    if (count <= 1) {
        return buttonClick()
    }
    var previousClickTime = 0L
    var clickTime: Long
    var clicksCounter = 0
    val click = click<Unit>()
    setOnClickListener {
        clickTime = System.currentTimeMillis()

        clicksCounter = clicksIntervalMillis
                .takeIf { clickTime - previousClickTime <= it }
                ?.let { clicksCounter + 1 } ?: 0
        previousClickTime = clickTime
        if (clicksCounter == count - 1) {
            click.backlash(Unit)
            clicksCounter = 0
            previousClickTime = 0
            clickTime = 0
        }
    }
    return click
}


private fun View.buttonClick(): Actions<Unit> {
    val click = click<Unit>()
    setOnClickListener { click.backlash(Unit) }
    return click
}
