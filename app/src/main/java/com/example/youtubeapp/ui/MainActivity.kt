package com.example.youtubeapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.youtubeapp.databinding.ActivityMainBinding
import com.example.youtubeapp.ui.adapter.VideoAdapter
import com.example.youtubeapp.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        setupSwipeRefresh()
        setupTabs()
        setupBottomNavigation()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(
            onVideoClick = { video ->
                // Handle video click - открыть детали видео
                Snackbar.make(
                    binding.root,
                    "Playing: ${video.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
            },
            onFavoriteClick = { video ->
                viewModel.toggleFavorite(video)
                Snackbar.make(
                    binding.root,
                    if (video.isFavorite) "Removed from favorites" else "Added to favorites",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )

        binding.recyclerView.apply {
            adapter = videoAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchVideos(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: implement real-time search
                return true
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupTabs() {
        binding.tabLayout.apply {
            addTab(newTab().setText("Home"))
            addTab(newTab().setText("Trending"))
            addTab(newTab().setText("Music"))
            addTab(newTab().setText("Gaming"))
            addTab(newTab().setText("News"))

            addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                    // Handle tab selection - load different categories
                    viewModel.loadTrendingVideos()
                }

                override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
                override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            })
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Handle navigation items
                else -> true
            }
        }
    }

    private fun observeViewModel() {
        // Observe videos
        viewModel.videos.observe(this) { videos ->
            videoAdapter.submitList(videos)
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = isLoading
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            error?.let {
                binding.tvError.apply {
                    text = it
                    visibility = View.VISIBLE
                }
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                    .setAction("Retry") { viewModel.loadTrendingVideos() }
                    .show()
                viewModel.clearError()
            } ?: run {
                binding.tvError.visibility = View.GONE
            }
        }

        // Observe cached videos (for offline mode)
        viewModel.cachedVideos.observe(this) { cachedVideos ->
            if (cachedVideos.isNotEmpty() && videoAdapter.currentList.isEmpty()) {
                videoAdapter.submitList(cachedVideos)
            }
        }
    }
}