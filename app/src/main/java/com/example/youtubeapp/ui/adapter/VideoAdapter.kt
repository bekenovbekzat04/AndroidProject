package com.example.youtubeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubeapp.R
import com.example.youtubeapp.data.model.Video
import com.example.youtubeapp.databinding.ItemVideoBinding

class VideoAdapter(
    private val onVideoClick: (Video) -> Unit,
    private val onFavoriteClick: (Video) -> Unit
) : ListAdapter<Video, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VideoViewHolder(
        private val binding: ItemVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.apply {
                tvVideoTitle.text = video.title
                tvChannelName.text = video.channelName
                tvVideoInfo.text = "${video.viewCount} views • ${video.publishedAt}"
                tvDuration.text = video.duration

                Glide.with(ivThumbnail.context)
                    .load(video.thumbnailUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivThumbnail)

                Glide.with(ivChannelAvatar.context)
                    .load(video.channelThumbnail)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .circleCrop()
                    .into(ivChannelAvatar)

                btnFavorite.setImageResource(
                    if (video.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )

                root.setOnClickListener { onVideoClick(video) }
                btnFavorite.setOnClickListener { onFavoriteClick(video) }
            }
        }
    }

    class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }
}