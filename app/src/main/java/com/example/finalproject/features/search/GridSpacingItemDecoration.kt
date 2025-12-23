package com.example.finalproject.features.search

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spacingPx: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacingPx / 2
        outRect.right = spacingPx / 2
        outRect.top = spacingPx / 2
        outRect.bottom = spacingPx / 2
    }
}
