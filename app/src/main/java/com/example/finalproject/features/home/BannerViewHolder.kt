package com.example.finalproject.features.home


import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.finalproject.R
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.databinding.ItemBannerBinding

class BannerViewHolder(
    private val binding: ItemBannerBinding,
    private val onAnimeClick: (Anime) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(anime: Anime) {
        binding.apply {
            bannerImage.load(anime.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_anime)
                error(R.drawable.placeholder_anime)
            }

            bannerTitle.text = anime.title
//            bannerDescription.text = anime.description.take(150) +
//                    if (anime.description.length > 150) "..." else ""

            continueButton.setOnClickListener { onAnimeClick(anime) }
            root.setOnClickListener { onAnimeClick(anime) }
        }
    }
}
