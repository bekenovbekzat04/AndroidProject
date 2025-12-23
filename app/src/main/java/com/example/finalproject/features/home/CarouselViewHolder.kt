package com.example.finalproject.features.home


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.databinding.ItemCarouselBinding

class CarouselViewHolder(
    private val binding: ItemCarouselBinding,
    onAnimeClick: (Anime) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val carouselAdapter = AnimeCarouselAdapter(onAnimeClick)

    init {
        binding.carouselRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = carouselAdapter
            setHasFixedSize(true)
        }
    }

    fun bind(section: HomeSection.Carousel) {
        binding.carouselTitle.text = section.title
        carouselAdapter.submitList(section.animeList)
    }
}
