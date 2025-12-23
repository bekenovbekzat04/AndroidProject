package com.example.finalproject.features.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.finalproject.R
import com.example.finalproject.core.domain.model.Anime
import com.example.finalproject.databinding.ItemAnimeCardBinding

class AnimeCarouselAdapter(
    private val onAnimeClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeCarouselAdapter.AnimeViewHolder>(AnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnimeViewHolder(
        private val binding: ItemAnimeCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.apply {
                animeImage.load(anime.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_anime)
                    error(R.drawable.placeholder_anime)
                }

                animeTitle.text = anime.title
                animeDescription.text = anime.description.take(80) +
                        if (anime.description.length > 80) "..." else ""

                root.setOnClickListener { onAnimeClick(anime) }
            }
        }
    }

    class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }
}
