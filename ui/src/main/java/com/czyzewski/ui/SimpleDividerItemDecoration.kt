package com.czyzewski.ui

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class SimpleDividerItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left: Int = parent.paddingLeft
        val right: Int = parent.width - parent.paddingRight
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams =
                child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val drawable = ContextCompat.getDrawable(this.context, R.drawable.divider_black)!!
            val bottom = top + drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c)
        }
    }


}