package com.example.finalproject.features.detail


import android.content.Context
import com.google.android.material.chip.Chip
import androidx.core.content.ContextCompat
import android.content.res.ColorStateList
import com.example.finalproject.R

fun Context.createGenreChip(text: String): Chip {
    val chip = Chip(this)
    chip.text = text
    chip.isCheckable = false
    chip.isClickable = false
    chip.setEnsureMinTouchTargetSize(false)
    chip.chipBackgroundColor =
        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.on_surface))
    chip.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
    chip.chipMinHeight = 36f
    chip.textSize = 12f
    val margin = (12 * resources.displayMetrics.density).toInt()
    chip.setPadding(margin, 8, margin, 8)
    return chip
}
