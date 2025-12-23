package com.example.finalproject.features.detail


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.content.Intent
import android.net.Uri
import coil.load
import com.example.finalproject.R
import com.example.finalproject.app.MainActivity
import com.example.finalproject.core.domain.model.AnimeDetail
import com.example.finalproject.databinding.FragmentAnimeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeDetailFragment : Fragment(R.layout.fragment_anime_detail) {

    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnimeDetailBinding.bind(view)

        val animeId = requireArguments().getInt(ARG_ANIME_ID)
        observeState()
        viewModel.load(animeId)

        binding.retryButton.setOnClickListener {
            viewModel.load(animeId)
        }
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        when (state) {
                            is AnimeDetailState.Idle -> Unit
                            is AnimeDetailState.Loading -> showLoading()
                            is AnimeDetailState.Success -> showContent(state.detail)
                            is AnimeDetailState.Error -> showError(state.message)
                        }
                    }
                }

                launch {
                    viewModel.isSaved.collect { saved ->
                        binding.saveButton.text = if (saved) {
                            getString(R.string.unsave)
                        } else {
                            getString(R.string.save)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setBottomNavigationVisible(false)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorGroup.visibility = View.GONE
        binding.contentScroll.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.contentScroll.visibility = View.GONE
        binding.errorGroup.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    private fun showContent(detail: AnimeDetail) {
        binding.progressBar.visibility = View.GONE
        binding.errorGroup.visibility = View.GONE
        binding.contentScroll.visibility = View.VISIBLE

        binding.posterImage.load(detail.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.placeholder_anime)
            error(R.drawable.placeholder_anime)
        }

        binding.titleText.text = detail.title
        binding.metaText.text = listOfNotNull(
            detail.year?.toString(),
            detail.status,
            detail.duration
        ).joinToString(" • ")

        binding.scoreText.text = detail.score?.let { "⭐ ${"%.1f".format(it)}" } ?: "⭐ N/A"
        binding.episodesText.text = detail.episodes?.let { "Episodes: $it" } ?: "Episodes: N/A"
        binding.durationText.text = detail.duration ?: "Duration: N/A"

        binding.statusChip.text = detail.status ?: "Status N/A"
        binding.ratingChip.text = detail.rating ?: "Rating N/A"
        renderGenres(detail.genres)
        binding.studiosText.text = if (detail.studios.isNotEmpty()) {
            "Studios: ${detail.studios.joinToString()}"
        } else {
            "Studios: N/A"
        }

        binding.synopsisText.text = detail.description.ifBlank { "No description available" }
        binding.backgroundText.text = detail.background ?: "No background info"

        if (!detail.trailerUrl.isNullOrBlank()) {
            binding.trailerButton.visibility = View.VISIBLE
            binding.trailerButton.setOnClickListener {
                openLink(detail.trailerUrl)
            }
        } else {
            binding.trailerButton.visibility = View.GONE
        }

        // external link fallback to trailerUrl for now
        if (!detail.trailerUrl.isNullOrBlank()) {
            binding.externalButton.visibility = View.VISIBLE
            binding.externalButton.setOnClickListener {
                openLink(detail.trailerUrl)
            }
        } else {
            binding.externalButton.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            viewModel.toggleSave(detail)
        }
    }

    private fun openLink(url: String) {
        runCatching {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun renderGenres(genres: List<String>) {
        binding.genresChipGroup.removeAllViews()
        if (genres.isEmpty()) {
            binding.genresText.visibility = View.VISIBLE
            binding.genresText.text = "No genres listed"
            return
        }
        binding.genresText.visibility = View.GONE
        genres.forEach { name ->
            val chip = requireContext().createGenreChip(name)
            binding.genresChipGroup.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setBottomNavigationVisible(true)
        _binding = null
    }

    companion object {
        private const val ARG_ANIME_ID = "arg_anime_id"

        fun newInstance(animeId: Int): AnimeDetailFragment {
            return AnimeDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ANIME_ID, animeId)
                }
            }
        }
    }
}
