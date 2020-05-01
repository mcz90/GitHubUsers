package com.czyzewski.ui

import androidx.lifecycle.LiveData

class Actions<T> : LiveData<T>() {

    fun backlash(value: T) {
        this.value = value
    }

    companion object {
        fun <T> click(): Actions<T> {
            return Actions()
        }
    }
}
