package com.idhub.shop.customview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


internal class SpaceItemDecoration(var mSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mSpace
    }
}