package com.example.finalproject.features.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentHomeBinding
import com.example.finalproject.features.detail.AnimeDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val adapter = HomeAdapter { anime -> openDetail(anime.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeState()

        viewModel.loadHomeData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is HomeState.Loading -> showLoading()
                        is HomeState.Success -> showContent(state)
                        is HomeState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorText.visibility = View.GONE
        }
    }

    private fun showContent(state: HomeState.Success) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorText.visibility = View.GONE
        }

        val sections = mutableListOf<HomeSection>()

        state.lastWatched?.let {
            sections.add(HomeSection.Banner(it))
        }

        sections.add(HomeSection.Carousel("Trending Now", state.trending))
        sections.add(HomeSection.Carousel("Popular", state.popular))
        sections.add(HomeSection.Carousel("Top Rated", state.topRated))

        adapter.submitList(sections)
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            errorText.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openDetail(animeId: Int) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container_view,
                AnimeDetailFragment.newInstance(animeId)
            )
            addToBackStack(null)
        }
    }
}
