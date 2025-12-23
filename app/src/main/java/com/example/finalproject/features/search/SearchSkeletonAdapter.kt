package com.example.finalproject.features.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R

class SearchSkeletonAdapter(
    private val itemCount: Int = 6
) : RecyclerView.Adapter<SearchSkeletonAdapter.SkeletonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkeletonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_anime_card_skeleton, parent, false)
        return SkeletonViewHolder(view)
    }

    override fun getItemCount(): Int = itemCount

    override fun onBindViewHolder(holder: SkeletonViewHolder, position: Int) = Unit

    class SkeletonViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
