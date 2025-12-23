package com.example.finalproject.features.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.finalproject.R
import com.example.finalproject.databinding.ItemAnimeCardBinding

class SearchAdapter(
    private val onClick: (SearchItem) -> Unit
) : ListAdapter<SearchItem, SearchAdapter.SearchViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemAnimeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        private val binding: ItemAnimeCardBinding,
        private val onClick: (SearchItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem) {
            binding.animeImage.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_anime)
                error(R.drawable.placeholder_anime)
            }
            binding.animeTitle.text = item.title
            binding.animeDescription.text = item.description.take(60) +
                    if (item.description.length > 60) "..." else ""
            binding.root.setOnClickListener { onClick(item) }

            val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            val margin = (12 * binding.root.resources.displayMetrics.density).toInt()
            params.setMargins(margin, margin, margin, margin)
            binding.root.layoutParams = params
        }
    }

    private object Diff : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
            oldItem == newItem
    }
}
