package com.example.finalproject.features.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.databinding.ItemBannerBinding
import com.example.finalproject.databinding.ItemCarouselBinding

class HomeAdapter(
    private val onAnimeClick: (Anime) -> Unit
) : ListAdapter<HomeSection, RecyclerView.ViewHolder>(HomeDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeSection.Banner -> VIEW_TYPE_BANNER
            is HomeSection.Carousel -> VIEW_TYPE_CAROUSEL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BANNER -> {
                val binding = ItemBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BannerViewHolder(binding, onAnimeClick)
            }
            VIEW_TYPE_CAROUSEL -> {
                val binding = ItemCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CarouselViewHolder(binding, onAnimeClick)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeSection.Banner -> (holder as BannerViewHolder).bind(item.anime)
            is HomeSection.Carousel -> (holder as CarouselViewHolder).bind(item)
        }
    }

    companion object {
        private const val VIEW_TYPE_BANNER = 0
        private const val VIEW_TYPE_CAROUSEL = 1
    }
}

class HomeDiffCallback : DiffUtil.ItemCallback<HomeSection>() {
    override fun areItemsTheSame(oldItem: HomeSection, newItem: HomeSection): Boolean {
        return when {
            oldItem is HomeSection.Banner && newItem is HomeSection.Banner ->
                oldItem.anime.id == newItem.anime.id
            oldItem is HomeSection.Carousel && newItem is HomeSection.Carousel ->
                oldItem.title == newItem.title
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: HomeSection, newItem: HomeSection): Boolean {
        return oldItem == newItem
    }
}
